package com.taskmanager.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public final class ResponseBuilder {

    private ResponseBuilder() {}

    public static ResponseEntity<?> success(Object data) {
        return success(data, "Success", HttpStatus.OK);
    }

    public static ResponseEntity<?> success(Object data, String message) {
        return success(data, message, HttpStatus.OK);
    }

    public static ResponseEntity<?> success(Object data, String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<?> created(Object data) {
        return created(data, "Resource created successfully");
    }

    public static ResponseEntity<?> created(Object data, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public static ResponseEntity<?> error(String message) {
        return error(message, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> error(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<?> error(String message, Object errors, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("errors", errors);
        response.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<?> notFound(String message) {
        return error(message, HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<?> unauthorized(String message) {
        return error(message, HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<?> forbidden(String message) {
        return error(message, HttpStatus.FORBIDDEN);
    }

    public static ResponseEntity<?> noContent() {
        return ResponseEntity.noContent().build();
    }

    public static Map<String, Object> paginate(Object data, long totalElements, int totalPages, int currentPage) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("totalElements", totalElements);
        pagination.put("totalPages", totalPages);
        pagination.put("currentPage", currentPage);
        pagination.put("pageSize", ((java.util.Collection<?>) data).size());
        
        response.put("pagination", pagination);
        return response;
    }
}
