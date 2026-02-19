
package com.taskmanager.controller;

import com.taskmanager.dto.TaskDTO;
import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.MessageResponse;
import com.taskmanager.dto.ErrorResponse;
import com.taskmanager.service.TaskService;
import com.taskmanager.util.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<?> getAllTasks(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority) {
        
        try {
            String username = jwtUtils.getUsernameFromToken(token.substring(7));
            
            Sort sort = sortDirection.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<TaskDTO> tasks = taskService.getUserTasks(username, status, priority, pageable);
            
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Error fetching tasks: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        
        try {
            String username = jwtUtils.getUsernameFromToken(token.substring(7));
            TaskDTO task = taskService.getTaskByIdAndUser(id, username);
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createTask(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody TaskRequest taskRequest) {
        
        try {
            String username = jwtUtils.getUsernameFromToken(token.substring(7));
            TaskDTO createdTask = taskService.createTask(username, taskRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Error creating task: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest taskRequest) {
        
        try {
            String username = jwtUtils.getUsernameFromToken(token.substring(7));
            TaskDTO updatedTask = taskService.updateTask(id, username, taskRequest);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Error updating task: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateTaskStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestParam String status) {
        
        try {
            String username = jwtUtils.getUsernameFromToken(token.substring(7));
            TaskDTO updatedTask = taskService.updateTaskStatus(id, username, status);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Invalid status value"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        
        try {
            String username = jwtUtils.getUsernameFromToken(token.substring(7));
            taskService.deleteTask(id, username);
            return ResponseEntity.ok(new MessageResponse("Task deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getTaskStats(@RequestHeader("Authorization") String token) {
        try {
            String username = jwtUtils.getUsernameFromToken(token.substring(7));
            TaskStats stats = taskService.getTaskStatistics(username);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Error fetching statistics"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchTasks(
            @RequestHeader("Authorization") String token,
            @RequestParam String keyword) {
        
        try {
            String username = jwtUtils.getUsernameFromToken(token.substring(7));
            List<TaskDTO> tasks = taskService.searchTasks(username, keyword);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Error searching tasks"));
        }
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<?> markTaskComplete(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        
        try {
            String username = jwtUtils.getUsernameFromToken(token.substring(7));
            TaskDTO task = taskService.markTaskComplete(id, username);
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/bulk-delete")
    public ResponseEntity<?> bulkDeleteTasks(
            @RequestHeader("Authorization") String token,
            @RequestBody List<Long> taskIds) {
        
        try {
            String username = jwtUtils.getUsernameFromToken(token.substring(7));
            int deletedCount = taskService.bulkDeleteTasks(taskIds, username);
            return ResponseEntity.ok(new MessageResponse(deletedCount + " tasks deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Error deleting tasks: " + e.getMessage()));
        }
    }
}
