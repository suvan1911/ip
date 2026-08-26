public class Event extends Task {
    private final String from;
    private final String to;

    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return String.format("%s (from: %s to: %s)", super.toString(), this.from, this.to);
    }

    @Override
    public String toSaveFormat() {
        return String.join(SAVE_FILE_SEPARATOR, super.toSaveFormat(), this.from, this.to);
    }
}
