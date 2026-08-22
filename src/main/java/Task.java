public abstract class Task {
    protected final String description;
    protected boolean completed;

    public Task(String description) {
        this.description = description;
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
        return "T";
    }

    @Override
    public String toString() {
        return String.format("[%s][%s] %s", getTaskIcon(), getStatusIcon(), this.description);
    }
}
