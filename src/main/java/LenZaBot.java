public class LenZaBot {
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
        quit();
    }

    public static void greet() {
        System.out.println("Hi. This is Lenza. What do you want to do?");
    }

    public static void quit() {
        System.out.println("Bye! See ya later.");
        System.exit(0);
    }
}
