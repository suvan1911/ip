package lenzabot;

/**
 * Represents user-facing errors specific to LenZaBot commands.
 */
public class LenZaBotException extends Exception {
    /**
     * Creates an exception with the given user-facing message.
     */
    public LenZaBotException(String message) {
        super(message);
    }
}
