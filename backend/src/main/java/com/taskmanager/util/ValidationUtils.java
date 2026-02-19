package com.taskmanager.util;

import java.util.regex.Pattern;

public final class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    private static final Pattern USERNAME_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9._-]{3,20}$");
    
    private static final Pattern PASSWORD_PATTERN = 
        Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^\\+?[0-9]{10,14}$");

    private ValidationUtils() {}

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidTaskTitle(String title) {
        return title != null && title.length() >= 3 && title.length() <= 200;
    }

    public static boolean isValidDescription(String description) {
        return description == null || description.length() <= 1000;
    }

    public static boolean isValidProgress(Integer progress) {
        return progress == null || (progress >= 0 && progress <= 100);
    }

    public static boolean isValidPriority(String priority) {
        if (priority == null) return true;
        return priority.equals("LOW") || priority.equals("MEDIUM") || 
               priority.equals("HIGH") || priority.equals("CRITICAL");
    }

    public static boolean isValidStatus(String status) {
        if (status == null) return true;
        return status.equals("PENDING") || status.equals("IN_PROGRESS") || 
               status.equals("COMPLETED") || status.equals("ON_HOLD") || 
               status.equals("CANCELLED") || status.equals("ARCHIVED");
    }

    public static boolean isValidCategory(String category) {
        return category == null || category.length() <= 50;
    }

    public static boolean isValidTags(String tags) {
        return tags == null || tags.length() <= 500;
    }

    public static boolean isValidEstimatedHours(Double hours) {
        return hours == null || (hours > 0 && hours <= 1000);
    }

    public static String sanitizeInput(String input) {
        if (input == null) return null;
        // Remove any HTML tags and trim
        return input.replaceAll("<[^>]*>", "").trim();
    }
}
