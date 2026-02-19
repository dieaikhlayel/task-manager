package com.taskmanager.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationErrorResponse {
    private int status;
    private String message;
    private String timestamp;
    private String path;
    private Map<String, String> errors;
    
    public ValidationErrorResponse(int status, String message, String timestamp, String path, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.path = path;
        this.errors = errors;
    }
}