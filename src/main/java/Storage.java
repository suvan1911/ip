import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles reading tasks from, and writing tasks to, a save file on disk so that
 * tasks survive across runs of the program.
 */
public class Storage {
    private final Path saveFilePath;

    /**
     * Creates a storage that reads and writes to the given save file path.
     */
    public Storage(Path saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    /**
     * Loads tasks from the save file.
     * Returns an empty list if the file does not exist yet; lines that are not
     * in a valid format are skipped instead of crashing the program.
     */
    public List<Task> loadTasks() {
        List<Task> loadedTasks = new ArrayList<>();
        if (!Files.exists(saveFilePath)) {
            return loadedTasks;
        }

        try {
            for (String line : Files.readAllLines(saveFilePath)) {
                Task task = parseTask(line);
                if (task != null) {
                    loadedTasks.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("Oops: could not read the save file. Starting with an empty list.");
        }
        return loadedTasks;
    }

    /**
     * Overwrites the save file with the given list of tasks, creating the
     * parent folder first if it does not exist yet.
     */
    public void saveTasks(List<Task> tasks) {
        try {
            if (saveFilePath.getParent() != null) {
                Files.createDirectories(saveFilePath.getParent());
            }
            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(task.toSaveFormat());
            }
            Files.write(saveFilePath, lines);
        } catch (IOException e) {
            System.out.println("Oops: could not save tasks to disk.");
        }
    }

    // Converts a saved line such as "D | 1 | return book | Friday" back into a
    // Task, or returns null if the line is corrupted or has an unknown format.
    private Task parseTask(String line) {
        String[] fields = line.split("\\Q" + Task.SAVE_FILE_SEPARATOR + "\\E", -1);
        if (fields.length < 3 || (!fields[1].equals("0") && !fields[1].equals("1"))) {
            return null;
        }

        boolean isCompleted = fields[1].equals("1");
        switch (fields[0]) {
        case "T":
            if (fields.length != 3 || fields[2].isEmpty()) {
                return null;
            }
            return createTask(new Todo(fields[2]), isCompleted);
        case "D":
            if (fields.length != 4 || fields[2].isEmpty() || fields[3].isEmpty()) {
                return null;
            }
            try {
                LocalDateTime by = LocalDateTime.parse(fields[3]);
                return createTask(new Deadline(fields[2], by), isCompleted);
            } catch (DateTimeParseException e) {
                return null;
            }
        case "E":
            if (fields.length != 5 || fields[2].isEmpty() || fields[3].isEmpty() || fields[4].isEmpty()) {
                return null;
            }
            try {
                LocalDateTime from = LocalDateTime.parse(fields[3]);
                LocalDateTime to = LocalDateTime.parse(fields[4]);
                return createTask(new Event(fields[2], from, to), isCompleted);
            } catch (DateTimeParseException e) {
                return null;
            }
        default:
            return null;
        }
    }

    // Applies the saved completion status to a freshly created task.
    private Task createTask(Task task, boolean isCompleted) {
        if (isCompleted) {
            task.markAsCompleted();
        }
        return task;
    }
}
