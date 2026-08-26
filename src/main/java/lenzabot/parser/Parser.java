package lenzabot.parser;

/**
 * Breaks raw user input into the command word and its argument.
 */
public class Parser {
    private Parser() {
    }

    /**
     * Represents user input split into its command word and argument.
     */
    public static class ParsedInput {
        private final String command;
        private final String argument;

        /**
         * Creates parsed input with a command word and argument.
         */
        public ParsedInput(String command, String argument) {
            this.command = command;
            this.argument = argument;
        }

        public String getCommand() {
            return command;
        }

        public String getArgument() {
            return argument;
        }
    }

    /**
     * Splits the given input at its first space into a command word and an
     * argument. Input without a space yields an empty argument.
     */
    public static ParsedInput parse(String input) {
        int firstSpaceIndex = input.indexOf(' ');
        if (firstSpaceIndex == -1) {
            return new ParsedInput(input, "");
        }
        return new ParsedInput(
                input.substring(0, firstSpaceIndex),
                input.substring(firstSpaceIndex + 1).trim()
        );
    }
}
