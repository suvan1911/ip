public abstract class Task {
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
}
