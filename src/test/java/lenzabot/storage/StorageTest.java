package lenzabot.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import lenzabot.task.Deadline;
import lenzabot.task.Event;
import lenzabot.task.Task;
import lenzabot.task.Todo;

class StorageTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    void saveTasks_multipleTaskTypes_writesAllTasksInOrder() throws IOException {
        Path saveFilePath = temporaryDirectory.resolve("data").resolve("lenzabot.txt");
        Storage storage = new Storage(saveFilePath);
        Task completedDeadline = new Deadline(
                "return book",
                LocalDateTime.of(2019, 12, 2, 18, 0)
        );
        completedDeadline.markAsCompleted();
        List<Task> tasks = List.of(
                new Todo("read book"),
                completedDeadline,
                new Event(
                        "project meeting",
                        LocalDateTime.of(2019, 12, 3, 14, 0),
                        LocalDateTime.of(2019, 12, 3, 16, 0)
                )
        );

        storage.saveTasks(tasks);

        assertEquals(
                List.of(
                        "T | 0 | read book",
                        "D | 1 | return book | 2019-12-02T18:00",
                        "E | 0 | project meeting | 2019-12-03T14:00 | 2019-12-03T16:00"
                ),
                Files.readAllLines(saveFilePath)
        );
    }
}
