package lenzabot.task;

/**
 * Enumerates the supported task categories and their display icons.
 */
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String icon;

    TaskType(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }
}
