package com.taskmanager.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime dueDate;
    private LocalDateTime completedDate;
    private String category;
    private String tags;
    private Double estimatedHours;
    private Double actualHours;
    private Integer progressPercentage;
    private boolean archived;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean overdue;
    
    // User info
    private Long userId;
    private String username;
}
