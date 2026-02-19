package com.taskmanager.repository;

import com.taskmanager.model.Category;
import com.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUser(User user);
    
    List<Category> findByUserOrderByNameAsc(User user);
    
    Optional<Category> findByUserAndName(User user, String name);
    
    Optional<Category> findByUserAndId(User user, Long id);
    
    @Query("SELECT c FROM Category c WHERE c.user = :user OR c.isDefault = true ORDER BY c.isDefault DESC, c.name ASC")
    List<Category> findUserCategoriesWithDefaults(@Param("user") User user);
    
    boolean existsByUserAndName(User user, String name);
    
    @Query("SELECT c FROM Category c WHERE c.user = :user AND c.taskCount > 0")
    List<Category> findCategoriesWithTasks(@Param("user") User user);
    
    @Modifying
    @Transactional
    @Query("UPDATE Category c SET c.taskCount = c.taskCount + 1 WHERE c.id = :id")
    void incrementTaskCount(@Param("id") Long id);
    
    @Modifying
    @Transactional
    @Query("UPDATE Category c SET c.taskCount = c.taskCount - 1 WHERE c.id = :id AND c.taskCount > 0")
    void decrementTaskCount(@Param("id") Long id);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Category c WHERE c.user = :user AND c.isDefault = false")
    void deleteUserCustomCategories(@Param("user") User user);
}
