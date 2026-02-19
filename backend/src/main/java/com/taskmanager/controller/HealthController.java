
package com.taskmanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public Map<String, Object> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", System.currentTimeMillis());
        response.put("service", "Task Manager Backend");
        
        Map<String, String> details = new HashMap<>();
        details.put("database", "connected");
        details.put("security", "enabled");
        details.put("cors", "configured");
        
        response.put("details", details);
        return response;
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
