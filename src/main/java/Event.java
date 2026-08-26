import java.time.LocalDateTime;

public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return String.format(
            "%s (from: %s to: %s)",
            super.toString(),
            DateTimeParser.format(this.from),
            DateTimeParser.format(this.to)
        );
    }

    @Override
    public String toSaveFormat() {
        return String.join(
            SAVE_FILE_SEPARATOR,
            super.toSaveFormat(),
            this.from.toString(),
            this.to.toString()
        );
    }
}
