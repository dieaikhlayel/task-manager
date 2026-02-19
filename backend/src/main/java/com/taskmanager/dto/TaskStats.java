package com.taskmanager.dto;

import lombok.Data;

@Data
public class TaskStats {
    private long totalTasks;
    private long completedTasks;
    private long pendingTasks;
    private long inProgressTasks;
    private long highPriorityTasks;
    private long mediumPriorityTasks;
    private long lowPriorityTasks;
    private double completionRate;
}
EOF