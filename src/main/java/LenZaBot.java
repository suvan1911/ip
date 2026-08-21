import java.util.Scanner;

public class LenZaBot {
    private static final String BYE_COMMAND = "bye";
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

            if (command.equals(BYE_COMMAND)) {
                quit();
            }

            System.out.println(command);
        }

    }

    public static void greet() {
        System.out.println("Hi. This is Lenza. What do you want to do?");
    }

    public static void quit() {
        System.out.println("Bye! See ya later.");
        System.exit(0);
    }
}
