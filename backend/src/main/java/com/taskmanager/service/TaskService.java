package com.taskmanager.service;

import com.taskmanager.dto.TaskDTO;
import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskStats;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.exception.ForbiddenException;
import com.taskmanager.exception.BadRequestException;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.model.TaskPriority;
import com.taskmanager.model.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public List<TaskDTO> getUserTasks(String username, String status) {
        log.debug("Fetching tasks for user: {} with status: {}", username, status);
        
        User user = getUserByUsername(username);
        
        if (status != null && !status.isEmpty()) {
            TaskStatus taskStatus = TaskStatus.fromString(status);
            return taskRepository.findByUserAndStatus(user, taskStatus)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        }
        
        return taskRepository.findByUser(user)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public Page<TaskDTO> getUserTasksPaginated(String username, String status, String priority, Pageable pageable) {
        log.debug("Fetching paginated tasks for user: {}", username);
        
        User user = getUserByUsername(username);
        
        if (status != null && priority != null) {
            TaskStatus taskStatus = TaskStatus.fromString(status);
            TaskPriority taskPriority = TaskPriority.fromString(priority);
            return taskRepository.findByUserAndStatusAndPriority(user, taskStatus, taskPriority, pageable)
                .map(this::convertToDTO);
        } else if (status != null) {
            TaskStatus taskStatus = TaskStatus.fromString(status);
            return taskRepository.findByUserAndStatus(user, taskStatus, pageable)
                .map(this::convertToDTO);
        } else if (priority != null) {
            TaskPriority taskPriority = TaskPriority.fromString(priority);
            return taskRepository.findByUserAndPriority(user, taskPriority, pageable)
                .map(this::convertToDTO);
        }
        
        return taskRepository.findByUser(user, pageable)
            .map(this::convertToDTO);
    }

    public TaskDTO getTaskByIdAndUser(Long taskId, String username) {
        log.debug("Fetching task id: {} for user: {}", taskId, username);
        
        Task task = getTaskById(taskId);
        validateTaskOwnership(task, username);
        
        return convertToDTO(task);
    }

    public TaskDTO createTask(String username, TaskRequest request) {
        log.info("Creating new task for user: {}", username);
        
        User user = getUserByUsername(username);
        
        // Validate due date
        if (request.getDueDate() != null && request.getDueDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Due date cannot be in the past");
        }
        
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.PENDING);
        task.setPriority(request.getPriority() != null ? 
            TaskPriority.fromString(request.getPriority()) : TaskPriority.MEDIUM);
        task.setDueDate(request.getDueDate());
        task.setCategory(request.getCategory());
        task.setTags(request.getTags());
        task.setEstimatedHours(request.getEstimatedHours());
        task.setUser(user);
        
        Task savedTask = taskRepository.save(task);
        log.info("Task created successfully with id: {}", savedTask.getId());
        
        return convertToDTO(savedTask);
    }

    public TaskDTO updateTask(Long taskId, String username, TaskRequest request) {
        log.info("Updating task id: {} for user: {}", taskId, username);
        
        Task task = getTaskById(taskId);
        validateTaskOwnership(task, username);
        
        // Update fields
        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            TaskStatus newStatus = TaskStatus.fromString(request.getStatus());
            task.setStatus(newStatus);
            
            // If task is completed, set completed date
            if (newStatus == TaskStatus.COMPLETED && task.getCompletedDate() == null) {
                task.setCompletedDate(LocalDateTime.now());
                task.setProgressPercentage(100);
            }
        }
        if (request.getPriority() != null) {
            task.setPriority(TaskPriority.fromString(request.getPriority()));
        }
        if (request.getDueDate() != null) {
            if (request.getDueDate().isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Due date cannot be in the past");
            }
            task.setDueDate(request.getDueDate());
        }
        if (request.getCategory() != null) {
            task.setCategory(request.getCategory());
        }
        if (request.getTags() != null) {
            task.setTags(request.getTags());
        }
        if (request.getEstimatedHours() != null) {
            task.setEstimatedHours(request.getEstimatedHours());
        }
        if (request.getProgressPercentage() != null) {
            if (request.getProgressPercentage() < 0 || request.getProgressPercentage() > 100) {
                throw new BadRequestException("Progress percentage must be between 0 and 100");
            }
            task.setProgressPercentage(request.getProgressPercentage());
            
            // If progress is 100, mark as completed
            if (request.getProgressPercentage() == 100 && task.getStatus() != TaskStatus.COMPLETED) {
                task.setStatus(TaskStatus.COMPLETED);
                task.setCompletedDate(LocalDateTime.now());
            }
        }
        
        Task updatedTask = taskRepository.save(task);
        log.info("Task updated successfully");
        
        return convertToDTO(updatedTask);
    }

    public TaskDTO updateTaskStatus(Long taskId, String username, String status) {
        log.info("Updating task status: {} for task id: {}", status, taskId);
        
        Task task = getTaskById(taskId);
        validateTaskOwnership(task, username);
        
        TaskStatus newStatus = TaskStatus.fromString(status);
        task.setStatus(newStatus);
        
        if (newStatus == TaskStatus.COMPLETED) {
            task.setCompletedDate(LocalDateTime.now());
            task.setProgressPercentage(100);
        }
        
        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }

    public void deleteTask(Long taskId, String username) {
        log.info("Deleting task id: {} for user: {}", taskId, username);
        
        Task task = getTaskById(taskId);
        validateTaskOwnership(task, username);
        
        taskRepository.delete(task);
        log.info("Task deleted successfully");
    }

    public TaskStats getTaskStatistics(String username) {
        log.debug("Calculating task statistics for user: {}", username);
        
        User user = getUserByUsername(username);
        
        List<Object[]> stats = taskRepository.getTaskStatistics(user);
        Object[] stat = stats.isEmpty() ? new Object[6] : stats.get(0);
        
        TaskStats taskStats = new TaskStats();
        taskStats.setTotalTasks(stat[0] != null ? ((Number) stat[0]).longValue() : 0);
        taskStats.setCompletedTasks(stat[1] != null ? ((Number) stat[1]).longValue() : 0);
        taskStats.setPendingTasks(stat[2] != null ? ((Number) stat[2]).longValue() : 0);
        taskStats.setInProgressTasks(stat[3] != null ? ((Number) stat[3]).longValue() : 0);
        taskStats.setHighPriorityTasks(stat[4] != null ? ((Number) stat[4]).longValue() : 0);
        
        // Calculate completion rate
        if (taskStats.getTotalTasks() > 0) {
            double rate = (double) taskStats.getCompletedTasks() / taskStats.getTotalTasks() * 100;
            taskStats.setCompletionRate(Math.round(rate * 100.0) / 100.0);
        }
        
        return taskStats;
    }

    public List<TaskDTO> searchTasks(String username, String keyword) {
        log.debug("Searching tasks for user: {} with keyword: {}", username, keyword);
        
        User user = getUserByUsername(username);
        
        return taskRepository.searchUserTasks(user, keyword)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TaskDTO> getOverdueTasks(String username) {
        log.debug("Fetching overdue tasks for user: {}", username);
        
        User user = getUserByUsername(username);
        
        return taskRepository.findOverdueTasksByUser(user, LocalDateTime.now())
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TaskDTO> getTasksDueToday(String username) {
        log.debug("Fetching tasks due today for user: {}", username);
        
        User user = getUserByUsername(username);
        
        return taskRepository.findTasksDueTodayByUser(user)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TaskDTO> getUpcomingTasks(String username) {
        log.debug("Fetching upcoming tasks for user: {}", username);
        
        User user = getUserByUsername(username);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextWeek = now.plusDays(7);
        
        return taskRepository.findUpcomingTasks(user, now, nextWeek)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public TaskDTO markTaskComplete(Long taskId, String username) {
        log.info("Marking task as complete: {}", taskId);
        
        Task task = getTaskById(taskId);
        validateTaskOwnership(task, username);
        
        task.markAsComplete();
        Task updatedTask = taskRepository.save(task);
        
        return convertToDTO(updatedTask);
    }

    public int bulkDeleteTasks(List<Long> taskIds, String username) {
        log.info("Bulk deleting {} tasks for user: {}", taskIds.size(), username);
        
        User user = getUserByUsername(username);
        int deletedCount = 0;
        
        for (Long taskId : taskIds) {
            try {
                Task task = getTaskById(taskId);
                if (task.getUser().equals(user)) {
                    taskRepository.delete(task);
                    deletedCount++;
                }
            } catch (Exception e) {
                log.warn("Failed to delete task {}: {}", taskId, e.getMessage());
            }
        }
        
        log.info("Successfully deleted {} tasks", deletedCount);
        return deletedCount;
    }

    public List<String> getAllCategories(String username) {
        log.debug("Fetching all categories for user: {}", username);
        
        User user = getUserByUsername(username);
        return taskRepository.findDistinctCategoriesByUser(user);
    }

    // Private helper methods
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    private Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
    }

    private void validateTaskOwnership(Task task, String username) {
        if (!task.getUser().getUsername().equals(username)) {
            throw new ForbiddenException("You don't have permission to access this task");
        }
    }

    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus().name());
        dto.setPriority(task.getPriority().name());
        dto.setDueDate(task.getDueDate());
        dto.setCompletedDate(task.getCompletedDate());
        dto.setCategory(task.getCategory());
        dto.setTags(task.getTags());
        dto.setEstimatedHours(task.getEstimatedHours());
        dto.setActualHours(task.getActualHours());
        dto.setProgressPercentage(task.getProgressPercentage());
        dto.setArchived(task.isArchived());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        dto.setOverdue(task.isOverdue());
        
        if (task.getUser() != null) {
            dto.setUserId(task.getUser().getId());
            dto.setUsername(task.getUser().getUsername());
        }
        
        return dto;
    }
}
