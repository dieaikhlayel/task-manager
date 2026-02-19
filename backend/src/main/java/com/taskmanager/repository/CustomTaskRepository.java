package com.taskmanager.repository;

import com.taskmanager.model.Task;
import com.taskmanager.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface CustomTaskRepository {
    
    // Complex search with multiple criteria
    List<Task> findTasksByCriteria(User user, 
                                    String searchTerm,
                                    String status,
                                    String priority,
                                    String category,
                                    LocalDateTime dueDateFrom,
                                    LocalDateTime dueDateTo,
                                    Boolean archived);
    
    // Get task summary report
    Map<String, Object> getTaskSummaryReport(User user, 
                                              LocalDateTime startDate, 
                                              LocalDateTime endDate);
    
    // Bulk update operations
    int bulkUpdateTasks(List<Long> taskIds, 
                        String status, 
                        String priority, 
                        String category);
    
    // Get productivity metrics
    Map<String, Double> getProductivityMetrics(User user, 
                                                LocalDateTime startDate, 
                                                LocalDateTime endDate);
}
