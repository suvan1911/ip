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

            String[] parts = input.split(" ", 2);
            String command = parts[0];
            String argument = parts.length > 1 ? parts[1] : "";

            switch (command) {
                case BYE_COMMAND -> handleByeCommand();
                case LIST_COMMAND -> handleListCommand();
                case MARK_COMMAND -> handleMarkCommand(argument);
                case UNMARK_COMMAND -> handleUnmarkCommand(argument);
                case TODO_COMMAND -> handleTodoCommand(argument);
                case DEADLINE_COMMAND -> handleDeadlineCommand(argument);
                case EVENT_COMMAND -> handleEventCommand(argument);
                default -> handleDefaultCommand(input);
            }
        }
    }

    public static void greet() {
        System.out.println("Hi. This is Lenza. What do you want to do?");
    }

    public static void quit() {
        System.out.println("Bye! See ya later.");
        System.exit(0);
    }

    public static void handleByeCommand() {
        quit();
    }

    public static void handleListCommand() {
        for (int i = 0; i < numTasks; i++) {
            System.out.println(
                String.format("%d. %s", i + 1, tasks[i])
            );
        }
    }

    public static void handleMarkCommand(String argument) {
        Task task = getTaskByIndex(Integer.parseInt(argument));
        task.markAsCompleted();
        System.out.println(
            String.format("Good job, marked the following task as completed: %s", task)
        );
    }

    public static void handleUnmarkCommand(String argument) {
        Task task = getTaskByIndex(Integer.parseInt(argument));
        task.markAsIncomplete();
        System.out.println(
            String.format("Ok, marked the following task as incomplete: %s", task)
        );
    }

    public static void handleTodoCommand(String argument) {
        addTask(new Todo(argument));
    }

    public static void handleDeadlineCommand(String argument) {
        String[] parts = argument.split(" /by ", 2);
        addTask(new Deadline(parts[0], parts[1]));
    }

    public static void handleEventCommand(String argument) {
        String[] descriptionAndSchedule = argument.split(" /from ", 2);
        String[] timeRange = descriptionAndSchedule[1].split(" /to ", 2);
        addTask(new Event(descriptionAndSchedule[0], timeRange[0], timeRange[1]));
    }

    public static void handleDefaultCommand(String input) {
        System.out.println("I dont undertsand. Please ask chatgpt :)");
    }

    // Returns the task associated to the one-based index (as shown by printTasks)
    public static Task getTaskByIndex(int index) {
        return tasks[index - 1];
    }

    public static void addTask(Task task) {
        tasks[numTasks++] = task;
        System.out.println(String.format("Added task: %s", task));
    }
}
