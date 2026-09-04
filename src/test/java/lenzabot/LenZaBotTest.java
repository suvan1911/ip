package lenzabot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import lenzabot.storage.Storage;

class LenZaBotTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    void getResponse_addThenList_returnsStoredTask() {
        LenZaBot lenZaBot = createLenZaBot();

        assertEquals("Added task: [T][ ] read book", lenZaBot.getResponse("todo read book"));
        assertEquals("1. [T][ ] read book", lenZaBot.getResponse("list"));
    }

    @Test
    void getResponse_invalidCommand_returnsUserFacingError() {
        LenZaBot lenZaBot = createLenZaBot();

        assertEquals("Oops: I dont understand what you mean by \"dance\".", lenZaBot.getResponse("dance"));
    }

    @Test
    void getResponse_deleteTask_returnsMultilineConfirmation() {
        LenZaBot lenZaBot = createLenZaBot();
        lenZaBot.getResponse("todo read book");

        String expected = String.join(System.lineSeparator(),
                "Noted. I've removed this task:",
                "  [T][ ] read book",
                "Now you have 0 tasks in the list.");
        assertEquals(expected, lenZaBot.getResponse("delete 1"));
    }

    private LenZaBot createLenZaBot() {
        return new LenZaBot(new Storage(temporaryDirectory.resolve("data.txt")));
    }
}
