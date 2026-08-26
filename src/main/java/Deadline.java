import java.time.LocalDateTime;

public class Deadline extends Task {
    private final LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    @Override
    public String toString() {
        return String.format("%s (by: %s)", super.toString(), DateTimeParser.format(this.by));
    }

    @Override
    public String toSaveFormat() {
        return String.join(SAVE_FILE_SEPARATOR, super.toSaveFormat(), this.by.toString());
    }
}
