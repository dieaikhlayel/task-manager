
package com.taskmanager.model;

public enum TaskPriority {
    LOW("Low", 1),
    MEDIUM("Medium", 2),
    HIGH("High", 3),
    CRITICAL("Critical", 4);

    private final String displayName;
    private final int level;

    TaskPriority(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLevel() {
        return level;
    }

    public static TaskPriority fromString(String text) {
        for (TaskPriority priority : TaskPriority.values()) {
            if (priority.name().equalsIgnoreCase(text) || priority.displayName.equalsIgnoreCase(text)) {
                return priority;
            }
        }
        return TaskPriority.MEDIUM; // Default
    }

    public static TaskPriority fromLevel(int level) {
        for (TaskPriority priority : TaskPriority.values()) {
            if (priority.level == level) {
                return priority;
            }
        }
        return TaskPriority.MEDIUM;
    }
}
