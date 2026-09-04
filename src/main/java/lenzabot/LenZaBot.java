package lenzabot;

import java.nio.file.Path;
import java.util.StringJoiner;

import lenzabot.parser.DateTimeParser;
import lenzabot.parser.Parser;
import lenzabot.storage.Storage;
import lenzabot.task.Deadline;
import lenzabot.task.Event;
import lenzabot.task.Task;
import lenzabot.task.TaskList;
import lenzabot.task.Todo;
import lenzabot.ui.Ui;

/**
 * Entry point of the LenZaBot chatbot. Wires together the UI, parser, task
 * list, and storage, and routes user commands to their handlers.
 */
public class LenZaBot {
    private static final String BYE_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";
    private static final String DELETE_COMMAND = "delete";
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final String FIND_COMMAND = "find";

    private final Storage storage;
    private final Ui ui = new Ui();
    private final TaskList tasks;
    private boolean isRunning = true;

    /**
     * Creates a chatbot whose tasks are loaded from and saved to the given storage.
     *
     * @param storage Storage used to load and save tasks.
     */
    public LenZaBot(Storage storage) {
        this.storage = storage;
        this.tasks = new TaskList(storage.loadTasks());
    }

    /**
     * Starts LenZaBot using the default save-file location.
     *
     * @param args Command-line arguments, which LenZaBot does not use.
     */
    public static void main(String[] args) {
        Storage storage = new Storage(Path.of("data", "lenzabot.txt"));
        new LenZaBot(storage).run();
    }

    /**
     * Runs the read-execute loop until the user exits with the `bye` command.
     */
    public void run() {
        ui.showWelcome();

        while (isRunning) {
            String input = ui.readCommand();
            if (input.isEmpty()) {
                continue;
            }

            String response = getResponse(input);
            if (!response.isEmpty()) {
                ui.showMessage(response);
            }
        }
    }

    /**
     * Executes one user command and returns the response to display.
     *
     * @param input User command to execute.
     * @return Response generated for the command.
     */
    public String getResponse(String input) {
        try {
            return executeCommand(input.trim());
        } catch (LenZaBotException exception) {
            return "Oops: " + exception.getMessage();
        }
    }

    private String executeCommand(String input) throws LenZaBotException {
        Parser.ParsedInput parsedInput = Parser.parse(input);

        return switch (parsedInput.getCommand()) {
            case BYE_COMMAND -> handleByeCommand(parsedInput.getArgument());
            case LIST_COMMAND -> handleListCommand(parsedInput.getArgument());
            case MARK_COMMAND -> handleMarkCommand(parsedInput.getArgument());
            case UNMARK_COMMAND -> handleUnmarkCommand(parsedInput.getArgument());
            case DELETE_COMMAND -> handleDeleteCommand(parsedInput.getArgument());
            case TODO_COMMAND -> handleTodoCommand(parsedInput.getArgument());
            case DEADLINE_COMMAND -> handleDeadlineCommand(parsedInput.getArgument());
            case EVENT_COMMAND -> handleEventCommand(parsedInput.getArgument());
            case FIND_COMMAND -> handleFindCommand(parsedInput.getArgument());
            default -> handleDefaultCommand(parsedInput.getCommand());
        };
    }

    private String handleByeCommand(String argument) throws LenZaBotException {
        ensureNoArgument(BYE_COMMAND, argument);
        isRunning = false;
        return "Bye! See ya later.";
    }

    private String handleListCommand(String argument) throws LenZaBotException {
        ensureNoArgument(LIST_COMMAND, argument);
        StringJoiner response = new StringJoiner(System.lineSeparator());
        int number = 1;
        for (Task task : tasks.getAllTasks()) {
            response.add(String.format("%d. %s", number, task));
            number++;
        }
        return response.toString();
    }

    private String handleMarkCommand(String argument) throws LenZaBotException {
        Task task = tasks.markTask(parseTaskIndex(argument, MARK_COMMAND));
        saveTasks();
        return String.format("Good job, marked the following task as completed: %s", task);
    }

    private String handleUnmarkCommand(String argument) throws LenZaBotException {
        Task task = tasks.unmarkTask(parseTaskIndex(argument, UNMARK_COMMAND));
        saveTasks();
        return String.format("Ok, marked the following task as incomplete: %s", task);
    }

    private String handleDeleteCommand(String argument) throws LenZaBotException {
        int taskIndex = parseTaskIndex(argument, DELETE_COMMAND);
        Task removedTask = tasks.deleteTask(taskIndex);
        saveTasks();
        return String.join(System.lineSeparator(),
                "Noted. I've removed this task:",
                "  " + removedTask,
                String.format("Now you have %d tasks in the list.", tasks.getSize()));
    }

    private String handleTodoCommand(String argument) throws LenZaBotException {
        ensureNonEmpty(TODO_COMMAND, argument);
        return addTask(new Todo(argument));
    }

    private String handleDeadlineCommand(String argument) throws LenZaBotException {
        ensureNonEmpty(DEADLINE_COMMAND, argument);

        int byMarkerIndex = argument.indexOf(" /by ");
        if (byMarkerIndex == -1) {
            throw new LenZaBotException("use `deadline <description> /by <time>`.");
        }

        String description = argument.substring(0, byMarkerIndex).trim();
        String by = argument.substring(byMarkerIndex + 5).trim();
        if (description.isEmpty() || by.isEmpty()) {
            throw new LenZaBotException("deadline needs both a description and a `/by` value.");
        }

        return addTask(new Deadline(description, DateTimeParser.parse(by)));
    }

    private String handleEventCommand(String argument) throws LenZaBotException {
        ensureNonEmpty(EVENT_COMMAND, argument);

        int fromMarkerIndex = argument.indexOf(" /from ");
        int toMarkerIndex = argument.indexOf(" /to ");
        if (fromMarkerIndex == -1 || toMarkerIndex == -1 || toMarkerIndex <= fromMarkerIndex) {
            throw new LenZaBotException("use `event <description> /from <start> /to <end>`.");
        }

        String description = argument.substring(0, fromMarkerIndex).trim();
        String from = argument.substring(fromMarkerIndex + 7, toMarkerIndex).trim();
        String to = argument.substring(toMarkerIndex + 5).trim();
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new LenZaBotException("event needs a description, `/from`, and `/to` values.");
        }

        return addTask(new Event(description, DateTimeParser.parse(from), DateTimeParser.parse(to)));
    }

    private String handleFindCommand(String argument) throws LenZaBotException {
        if (argument.isEmpty()) {
            throw new LenZaBotException("the keyword for `find` cannot be empty.");
        }

        StringJoiner response = new StringJoiner(System.lineSeparator());
        response.add("Here are the matching tasks in your list:");
        int number = 1;
        for (Task task : tasks.findTasks(argument)) {
            response.add(String.format("%d. %s", number, task));
            number++;
        }
        return response.toString();
    }

    private String handleDefaultCommand(String command) throws LenZaBotException {
        throw new LenZaBotException(
                String.format("I dont understand what you mean by \"%s\".", command)
        );
    }

    // Persists the given newly added task, then confirms the addition.
    private String addTask(Task task) {
        tasks.addTask(task);
        saveTasks();
        return String.format("Added task: %s", task);
    }

    // Saves the current task list through storage so changes survive restarts.
    private void saveTasks() {
        storage.saveTasks(tasks.getAllTasks());
    }

    private void ensureNoArgument(String command, String argument) throws LenZaBotException {
        if (!argument.isEmpty()) {
            throw new LenZaBotException("`" + command + "` does not take extra text.");
        }
    }

    private void ensureNonEmpty(String command, String argument) throws LenZaBotException {
        if (argument.isEmpty()) {
            throw new LenZaBotException("the description for `" + command + "` cannot be empty.");
        }
    }

    private int parseTaskIndex(String argument, String command) throws LenZaBotException {
        ensureNonEmpty(command, argument);

        try {
            return Integer.parseInt(argument);
        } catch (NumberFormatException exception) {
            throw new LenZaBotException("`" + command + "` needs a valid task number.");
        }
    }
}
