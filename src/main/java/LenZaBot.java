import java.util.Scanner;

public class LenZaBot {
    private static final String BYE_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";

    private static int numTasks = 0;
    private static String[] tasks = new String[100];
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

            switch (command) {
                case BYE_COMMAND -> quit();
                case LIST_COMMAND -> printTasks();
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

    public static void addTask(String task) {
        tasks[numTasks] = task;
        numTasks++;
        System.out.println(String.format("Added task: %s", task));
    }

    public static void printTasks() {
        for (int i = 0; i < numTasks; i++) {
            System.out.println(
                String.format("%d. %s", i + 1, tasks[i])
            );
        }
    }
}
