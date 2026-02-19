
package com.taskmanager.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing  // Enables JPA Auditing for createdAt/updatedAt
@EnableTransactionManagement
@EntityScan(basePackages = {"com.taskmanager.model"})
@EnableJpaRepositories(basePackages = {"com.taskmanager.repository"})
public class DatabaseConfig {
    
    // Additional database configuration if needed
    // For example, custom Hibernate properties or connection pool settings
}
