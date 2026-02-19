package com.taskmanager.repository;

import com.taskmanager.model.Task;
import com.taskmanager.model.TaskPriority;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Find tasks by user
    List<Task> findByUser(User user);
    
    Page<Task> findByUser(User user, Pageable pageable);
    
    // Find tasks by user id
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId")
    List<Task> findAllByUserId(@Param("userId") Long userId);
    
    Page<Task> findByUserId(Long userId, Pageable pageable);
    
    // Find by status
    List<Task> findByStatus(TaskStatus status);
    
    List<Task> findByUserAndStatus(User user, TaskStatus status);
    
    Page<Task> findByUserAndStatus(User user, TaskStatus status, Pageable pageable);
    
    // Find by priority
    List<Task> findByPriority(TaskPriority priority);
    
    List<Task> findByUserAndPriority(User user, TaskPriority priority);
    
    Page<Task> findByUserAndPriority(User user, TaskPriority priority, Pageable pageable);
    
    // Find by status and priority
    List<Task> findByStatusAndPriority(TaskStatus status, TaskPriority priority);
    
    List<Task> findByUserAndStatusAndPriority(User user, TaskStatus status, TaskPriority priority);
    
    Page<Task> findByUserAndStatusAndPriority(User user, TaskStatus status, TaskPriority priority, Pageable pageable);
    
    // Find overdue tasks
    @Query("SELECT t FROM Task t WHERE t.dueDate < :now AND t.status != 'COMPLETED'")
    List<Task> findOverdueTasks(@Param("now") LocalDateTime now);
    
    @Query("SELECT t FROM Task t WHERE t.user = :user AND t.dueDate < :now AND t.status != 'COMPLETED'")
    List<Task> findOverdueTasksByUser(@Param("user") User user, @Param("now") LocalDateTime now);
    
    // Find tasks due today
    @Query("SELECT t FROM Task t WHERE DATE(t.dueDate) = CURRENT_DATE AND t.status != 'COMPLETED'")
    List<Task> findTasksDueToday();
    
    @Query("SELECT t FROM Task t WHERE t.user = :user AND DATE(t.dueDate) = CURRENT_DATE AND t.status != 'COMPLETED'")
    List<Task> findTasksDueTodayByUser(@Param("user") User user);
    
    // Find tasks due this week
    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :start AND :end AND t.status != 'COMPLETED'")
    List<Task> findTasksDueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    // Search tasks by title or description
    @Query("SELECT t FROM Task t WHERE t.user = :user AND " +
           "(LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Task> searchUserTasks(@Param("user") User user, @Param("keyword") String keyword);
    
    // Find tasks by category
    List<Task> findByUserAndCategory(User user, String category);
    
    @Query("SELECT DISTINCT t.category FROM Task t WHERE t.user = :user AND t.category IS NOT NULL")
    List<String> findDistinctCategoriesByUser(@Param("user") User user);
    
    // Find tasks by tags
    @Query("SELECT t FROM Task t WHERE t.user = :user AND t.tags LIKE %:tag%")
    List<Task> findByTag(@Param("user") User user, @Param("tag") String tag);
    
    // Count tasks by status for a user
    @Query("SELECT t.status, COUNT(t) FROM Task t WHERE t.user = :user GROUP BY t.status")
    List<Object[]> countTasksByStatus(@Param("user") User user);
    
    // Count tasks by priority for a user
    @Query("SELECT t.priority, COUNT(t) FROM Task t WHERE t.user = :user GROUP BY t.priority")
    List<Object[]> countTasksByPriority(@Param("user") User user);
    
    // Get task statistics for a user
    @Query("SELECT " +
           "COUNT(t) as total, " +
           "SUM(CASE WHEN t.status = 'COMPLETED' THEN 1 ELSE 0 END) as completed, " +
           "SUM(CASE WHEN t.status = 'PENDING' THEN 1 ELSE 0 END) as pending, " +
           "SUM(CASE WHEN t.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) as inProgress, " +
           "SUM(CASE WHEN t.priority = 'HIGH' OR t.priority = 'CRITICAL' THEN 1 ELSE 0 END) as highPriority, " +
           "AVG(CASE WHEN t.status = 'COMPLETED' AND t.estimatedHours > 0 THEN t.actualHours/t.estimatedHours ELSE NULL END) as avgCompletionRate " +
           "FROM Task t WHERE t.user = :user")
    List<Object[]> getTaskStatistics(@Param("user") User user);
    
    // Update task status
    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.status = :status, t.completedDate = CASE WHEN :status = 'COMPLETED' THEN :now ELSE NULL END WHERE t.id = :id")
    int updateTaskStatus(@Param("id") Long id, @Param("status") TaskStatus status, @Param("now") LocalDateTime now);
    
    // Update task progress
    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.progressPercentage = :progress WHERE t.id = :id")
    int updateTaskProgress(@Param("id") Long id, @Param("progress") Integer progress);
    
    // Bulk archive completed tasks
    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.archived = true WHERE t.user = :user AND t.status = 'COMPLETED' AND t.completedDate < :date")
    int archiveCompletedTasks(@Param("user") User user, @Param("date") LocalDateTime date);
    
    // Delete archived tasks
    @Modifying
    @Transactional
    @Query("DELETE FROM Task t WHERE t.user = :user AND t.archived = true AND t.updatedAt < :date")
    int deleteArchivedTasks(@Param("user") User user, @Param("date") LocalDateTime date);
    
    // Get upcoming tasks (due in next 7 days, not completed)
    @Query("SELECT t FROM Task t WHERE t.user = :user AND t.status != 'COMPLETED' " +
           "AND t.dueDate BETWEEN :now AND :nextWeek ORDER BY t.dueDate ASC")
    List<Task> findUpcomingTasks(@Param("user") User user, 
                                 @Param("now") LocalDateTime now, 
                                 @Param("nextWeek") LocalDateTime nextWeek);
    
    // Get task timeline for a date range
    @Query("SELECT DATE(t.dueDate) as date, COUNT(t) as count " +
           "FROM Task t WHERE t.user = :user AND t.dueDate BETWEEN :start AND :end " +
           "GROUP BY DATE(t.dueDate) ORDER BY date")
    List<Object[]> getTaskTimeline(@Param("user") User user,
                                   @Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end);
}
EOF