/**
 * Breaks raw user input into the command word and its argument.
 */
public class Parser {

    /**
     * Represents user input split into its command word and argument.
     */
    public record ParsedInput(String command, String argument) {
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
