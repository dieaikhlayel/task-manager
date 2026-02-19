package com.taskmanager.security;

public final class SecurityConstants {
    
    private SecurityConstants() {}
    
    // JWT Constants
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String LOGIN_URL = "/api/auth/login";
    public static final String REGISTER_URL = "/api/auth/register";
    
    // Role Constants
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MODERATOR = "MODERATOR";
    
    // Public URLs (no authentication required)
    public static final String[] PUBLIC_URLS = {
        "/api/auth/**",
        "/api/test/public",
        "/api/health/**",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/h2-console/**"
    };
    
    // Admin only URLs
    public static final String[] ADMIN_URLS = {
        "/api/admin/**",
        "/api/users/**"
    };
}
