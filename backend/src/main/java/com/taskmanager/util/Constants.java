package com.taskmanager.util;

public final class Constants {

    private Constants() {}
    
    // API Versions
    public static final String API_VERSION = "v1";
    public static final String API_BASE_PATH = "/api/" + API_VERSION;
    
    // Pagination defaults
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_FIELD = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION = "desc";
    
    // Task constants
    public static final String TASK_STATUS_PENDING = "PENDING";
    public static final String TASK_STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String TASK_STATUS_COMPLETED = "COMPLETED";
    public static final String TASK_STATUS_ON_HOLD = "ON_HOLD";
    
    public static final String TASK_PRIORITY_LOW = "LOW";
    public static final String TASK_PRIORITY_MEDIUM = "MEDIUM";
    public static final String TASK_PRIORITY_HIGH = "HIGH";
    public static final String TASK_PRIORITY_CRITICAL = "CRITICAL";
    
    // User roles
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MODERATOR = "MODERATOR";
    
    // Date formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    
    // Error messages
    public static final String ERROR_USER_NOT_FOUND = "User not found";
    public static final String ERROR_TASK_NOT_FOUND = "Task not found";
    public static final String ERROR_UNAUTHORIZED = "You are not authorized to perform this action";
    public static final String ERROR_INVALID_TOKEN = "Invalid or expired token";
    public static final String ERROR_DUPLICATE_USERNAME = "Username already exists";
    public static final String ERROR_DUPLICATE_EMAIL = "Email already exists";
    public static final String ERROR_INVALID_PASSWORD = "Invalid password";
    public static final String ERROR_PASSWORDS_DONT_MATCH = "Passwords do not match";
    
    // Success messages
    public static final String SUCCESS_USER_REGISTERED = "User registered successfully";
    public static final String SUCCESS_USER_LOGGED_IN = "User logged in successfully";
    public static final String SUCCESS_USER_LOGGED_OUT = "User logged out successfully";
    public static final String SUCCESS_TASK_CREATED = "Task created successfully";
    public static final String SUCCESS_TASK_UPDATED = "Task updated successfully";
    public static final String SUCCESS_TASK_DELETED = "Task deleted successfully";
    public static final String SUCCESS_TASK_COMPLETED = "Task marked as complete";
    
    // Cache names
    public static final String CACHE_USERS = "users";
    public static final String CACHE_TASKS = "tasks";
    public static final String CACHE_USER_TASKS = "userTasks";
    
    // Metrics
    public static final String METRIC_TASK_CREATED = "task.created";
    public static final String METRIC_TASK_COMPLETED = "task.completed";
    public static final String METRIC_TASK_DELETED = "task.deleted";
    public static final String METRIC_USER_REGISTERED = "user.registered";
    public static final String METRIC_USER_LOGGED_IN = "user.loggedIn";
}
