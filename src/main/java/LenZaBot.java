import java.util.Scanner;

public class LenZaBot {
    private static final String BYE_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";

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
            String command = scanner.nextLine();
            String[] parts = command.split(" ");

            switch (parts[0]) {
                case BYE_COMMAND -> quit();
                case LIST_COMMAND -> printTasks();
                case MARK_COMMAND -> markTaskComplete(getTaskByIndex(Integer.parseInt(parts[1])));
                case UNMARK_COMMAND -> markTaskIncomplete(getTaskByIndex(Integer.parseInt(parts[1])));
                default -> addTask(command);
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

    // Returns the task associated to the one-based index (as shown by printTasks)
    public static Task getTaskByIndex(int index) {
        return tasks[index - 1];
    }

    public static void addTask(String desc) {
        Task createdTask = new Task(desc, false);
        tasks[numTasks++] = createdTask;
        System.out.println(String.format("Added task: %s", createdTask));
    }

    public static void markTaskComplete(Task task) {
        task.markAsCompleted();
        System.out.println(
            String.format("Good job, marked the following task as completed: %s", task)
        );
    }

    public static void markTaskIncomplete(Task task) {
        task.markAsIncomplete();
        System.out.println(
            String.format("Ok, marked the following task as incomplete: %s", task)
        );
    }

    public static void printTasks() {
        for (int i = 0; i < numTasks; i++) {
            System.out.println(
                String.format("%d. %s", i + 1, tasks[i])
            );
        }
    }
}
