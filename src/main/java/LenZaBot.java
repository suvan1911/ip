import java.util.Scanner;

public class LenZaBot {
    private static final String BYE_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";

    private static int numTasks = 0;
    private static final Task[] tasks = new Task[100];

    public static void main(String[] args) {
        String banner = """
██      ███████ ███    ██ ███████  █████  ██████   ██████  ████████ 
██      ██      ████   ██    ███  ██   ██ ██   ██ ██    ██    ██    
██      █████   ██ ██  ██   ███   ███████ ██████  ██    ██    ██    
██      ██      ██  ██ ██  ███    ██   ██ ██   ██ ██    ██    ██    
███████ ███████ ██   ████ ███████ ██   ██ ██████   ██████     ██    
        """;

        System.out.println(banner);
        greet();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(">  ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                continue;
            }

            try {
                handleCommand(input);
            } catch (LenZaBotException e) {
                printError(e.getMessage());
            }
        }
    }

    public static void handleCommand(String input) throws LenZaBotException {
        int firstSpaceIndex = input.indexOf(' ');
        String command = firstSpaceIndex == -1 ? input : input.substring(0, firstSpaceIndex);
        String argument = firstSpaceIndex == -1 ? "" : input.substring(firstSpaceIndex + 1).trim();

        switch (command) {
            case BYE_COMMAND -> handleByeCommand(argument);
            case LIST_COMMAND -> handleListCommand(argument);
            case MARK_COMMAND -> handleMarkCommand(argument);
            case UNMARK_COMMAND -> handleUnmarkCommand(argument);
            case TODO_COMMAND -> handleTodoCommand(argument);
            case DEADLINE_COMMAND -> handleDeadlineCommand(argument);
            case EVENT_COMMAND -> handleEventCommand(argument);
            default -> handleDefaultCommand(command);
        }
    }

    public static void greet() {
        System.out.println("Hi. This is Lenza. What do you want to do?");
    }

    public static void quit() {
        System.out.println("Bye! See ya later.");
        System.exit(0);
    }

    public static void printError(String message) {
        System.out.println("Oops: " + message);
    }

    public static void handleByeCommand(String argument) throws LenZaBotException {
        ensureNoArgument(BYE_COMMAND, argument);
        quit();
    }

    public static void handleListCommand(String argument) throws LenZaBotException {
        ensureNoArgument(LIST_COMMAND, argument);
        for (int i = 0; i < numTasks; i++) {
            System.out.println(
                String.format("%d. %s", i + 1, tasks[i])
            );
        }
    }

    public static void handleMarkCommand(String argument) throws LenZaBotException {
        Task task = getTaskByIndex(parseTaskIndex(argument, MARK_COMMAND));
        task.markAsCompleted();
        System.out.println(
            String.format("Good job, marked the following task as completed: %s", task)
        );
    }

    public static void handleUnmarkCommand(String argument) throws LenZaBotException {
        Task task = getTaskByIndex(parseTaskIndex(argument, UNMARK_COMMAND));
        task.markAsIncomplete();
        System.out.println(
            String.format("Ok, marked the following task as incomplete: %s", task)
        );
    }

    public static void handleTodoCommand(String argument) throws LenZaBotException {
        ensureNonEmpty(TODO_COMMAND, argument);
        addTask(new Todo(argument));
    }

    public static void handleDeadlineCommand(String argument) throws LenZaBotException {
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

        addTask(new Deadline(description, by));
    }

    public static void handleEventCommand(String argument) throws LenZaBotException {
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

        addTask(new Event(description, from, to));
    }

    public static void handleDefaultCommand(String command) throws LenZaBotException {
        throw new LenZaBotException(
            String.format("I dont understand what you mean by \"%s\".", command)
        );
    }

    // Returns the task associated to the one-based index (as shown by printTasks)
    public static Task getTaskByIndex(int index) throws LenZaBotException {
        if (numTasks == 0) {
            throw new LenZaBotException("there are no tasks in the list yet.");
        }

        if (index < 1 || index > numTasks) {
            throw new LenZaBotException("task number must be between 1 and " + numTasks + ".");
        }

        return tasks[index - 1];
    }

    public static void addTask(Task task) throws LenZaBotException {
        if (numTasks >= tasks.length) {
            throw new LenZaBotException("the task list is full.");
        }

        tasks[numTasks++] = task;
        System.out.println(String.format("Added task: %s", task));
    }

    public static void ensureNoArgument(String command, String argument) throws LenZaBotException {
        if (!argument.isEmpty()) {
            throw new LenZaBotException("`" + command + "` does not take extra text.");
        }
    }

    public static void ensureNonEmpty(String command, String argument) throws LenZaBotException {
        if (argument.isEmpty()) {
            throw new LenZaBotException("the description for `" + command + "` cannot be empty.");
        }
    }

    public static int parseTaskIndex(String argument, String command) throws LenZaBotException {
        ensureNonEmpty(command, argument);

        try {
            return Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw new LenZaBotException("`" + command + "` needs a valid task number.");
        }
    }
}
