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
    void getResponse_validDeadline_returnsAddedDeadline() {
        LenZaBot lenZaBot = createLenZaBot();

        assertEquals(
                "Added task: [D][ ] return book (by: Dec 2 2019, 6:00 PM)",
                lenZaBot.getResponse("deadline return book /by 2/12/2019 1800")
        );
    }

    @Test
    void getResponse_validEvent_returnsAddedEvent() {
        LenZaBot lenZaBot = createLenZaBot();

        assertEquals(
                "Added task: [E][ ] project meeting (from: Dec 2 2019, 2:00 PM to: Dec 2 2019, 4:00 PM)",
                lenZaBot.getResponse("event project meeting /from 2/12/2019 1400 /to 2/12/2019 1600")
        );
    }

    @Test
    void getResponse_eventMarkersInWrongOrder_returnsUsageError() {
        LenZaBot lenZaBot = createLenZaBot();

        assertEquals(
                "Oops: use `event <description> /from <start> /to <end>`.",
                lenZaBot.getResponse("event project meeting /to 2/12/2019 1600 /from 2/12/2019 1400")
        );
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
