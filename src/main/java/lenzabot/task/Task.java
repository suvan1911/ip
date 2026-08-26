package lenzabot.task;

public abstract class Task {
    /** Separator between fields in the save file format. */
    public static final String SAVE_FILE_SEPARATOR = " | ";

    protected final String description;
    protected final TaskType taskType;
    protected boolean completed;

    /**
     * Creates a task with a description and a fixed task category.
     */
    public Task(String description, TaskType taskType) {
        this.description = description;
        this.taskType = taskType;
        this.completed = false;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public void markAsCompleted() {
        this.completed = true;
    }

    public void markAsIncomplete() {
        this.completed = false;
    }

    public String getStatusIcon() {
        return this.completed ? "X" : " ";
    }

    public String getTaskIcon() {
        return this.taskType.getIcon();
    }

    @Override
    public String toString() {
        return String.format("[%s][%s] %s", getTaskIcon(), getStatusIcon(), this.description);
    }

    /**
     * Returns the one-line format used by Storage to persist this task,
     * e.g. "T | 0 | read book" for an incomplete todo.
     */
    public String toSaveFormat() {
        return String.join(
            SAVE_FILE_SEPARATOR,
            getTaskIcon(),
            isCompleted() ? "1" : "0",
            this.description
        );
    }
}
