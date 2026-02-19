package com.taskmanager.repository;

import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomTaskRepositoryImpl implements CustomTaskRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Task> findTasksByCriteria(User user, 
                                           String searchTerm,
                                           String status,
                                           String priority,
                                           String category,
                                           LocalDateTime dueDateFrom,
                                           LocalDateTime dueDateTo,
                                           Boolean archived) {
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> query = cb.createQuery(Task.class);
        Root<Task> task = query.from(Task.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        // Always filter by user
        predicates.add(cb.equal(task.get("user"), user));
        
        // Add search term filter
        if (searchTerm != null && !searchTerm.isEmpty()) {
            Predicate titlePredicate = cb.like(cb.lower(task.get("title")), 
                                               "%" + searchTerm.toLowerCase() + "%");
            Predicate descPredicate = cb.like(cb.lower(task.get("description")), 
                                               "%" + searchTerm.toLowerCase() + "%");
            predicates.add(cb.or(titlePredicate, descPredicate));
        }
        
        // Add status filter
        if (status != null && !status.isEmpty()) {
            predicates.add(cb.equal(task.get("status"), 
                           cb.parameter(String.class, "status")));
        }
        
        // Add priority filter
        if (priority != null && !priority.isEmpty()) {
            predicates.add(cb.equal(task.get("priority"), 
                           cb.parameter(String.class, "priority")));
        }
        
        // Add category filter
        if (category != null && !category.isEmpty()) {
            predicates.add(cb.equal(task.get("category"), category));
        }
        
        // Add due date range filter
        if (dueDateFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(task.get("dueDate"), dueDateFrom));
        }
        
        if (dueDateTo != null) {
            predicates.add(cb.lessThanOrEqualTo(task.get("dueDate"), dueDateTo));
        }
        
        // Add archived filter
        if (archived != null) {
            predicates.add(cb.equal(task.get("archived"), archived));
        }
        
        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(task.get("dueDate")));
        
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Map<String, Object> getTaskSummaryReport(User user, 
                                                     LocalDateTime startDate, 
                                                     LocalDateTime endDate) {
        Map<String, Object> report = new HashMap<>();
        
        // This would typically use native queries or multiple JPQL queries
        // For simplicity, we'll return a placeholder
        report.put("totalTasks", 0L);
        report.put("completedTasks", 0L);
        report.put("pendingTasks", 0L);
        report.put("completionRate", 0.0);
        
        return report;
    }

    @Override
    public int bulkUpdateTasks(List<Long> taskIds, String status, String priority, String category) {
        // Implementation for bulk updates
        // This would use CriteriaUpdate for bulk operations
        return 0;
    }

    @Override
    public Map<String, Double> getProductivityMetrics(User user, 
                                                       LocalDateTime startDate, 
                                                       LocalDateTime endDate) {
        Map<String, Double> metrics = new HashMap<>();
        
        // Calculate productivity metrics
        metrics.put("tasksPerDay", 0.0);
        metrics.put("completionRate", 0.0);
        metrics.put("averageCompletionTime", 0.0);
        
        return metrics;
    }
}
