
package com.taskmanager.repository;

import com.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find by username (case-insensitive)
    Optional<User> findByUsername(String username);
    
    Optional<User> findByUsernameIgnoreCase(String username);
    
    // Find by email (case-insensitive)
    Optional<User> findByEmail(String email);
    
    Optional<User> findByEmailIgnoreCase(String email);
    
    // Check existence
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    // Find by username or email
    @Query("SELECT u FROM User u WHERE u.username = :login OR u.email = :login")
    Optional<User> findByUsernameOrEmail(@Param("login") String login);
    
    // Find users by role
    @Query("SELECT u FROM User u WHERE :role MEMBER OF u.roles")
    List<User> findAllByRole(@Param("role") String role);
    
    // Find users created after date
    List<User> findByCreatedAtAfter(LocalDateTime date);
    
    // Find users who haven't verified email
    List<User> findByEmailVerifiedFalse();
    
    // Find users who haven't logged in since date
    @Query("SELECT u FROM User u WHERE u.lastLogin < :date OR u.lastLogin IS NULL")
    List<User> findInactiveUsers(@Param("date") LocalDateTime date);
    
    // Update last login
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastLogin = :now WHERE u.username = :username")
    void updateLastLogin(@Param("username") String username, @Param("now") LocalDateTime now);
    
    // Search users by name or email
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> searchUsers(@Param("searchTerm") String searchTerm);
    
    // Count users by role
    @Query("SELECT COUNT(u) FROM User u WHERE :role MEMBER OF u.roles")
    long countByRole(@Param("role") String role);
    
    // Find users with most tasks
    @Query("SELECT u, COUNT(t) as taskCount FROM User u LEFT JOIN u.tasks t " +
           "GROUP BY u ORDER BY taskCount DESC")
    List<Object[]> findUsersWithMostTasks();
    
    // Update user profile
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.firstName = :firstName, u.lastName = :lastName, " +
           "u.profilePicture = :profilePicture WHERE u.id = :id")
    void updateProfile(@Param("id") Long id, 
                      @Param("firstName") String firstName,
                      @Param("lastName") String lastName,
                      @Param("profilePicture") String profilePicture);
    
    // Verify email
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.emailVerified = true WHERE u.id = :id")
    void verifyEmail(@Param("id") Long id);
    
    // Delete inactive users (admin function)
    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.emailVerified = false AND u.createdAt < :date")
    int deleteUnverifiedUsers(@Param("date") LocalDateTime date);
}
