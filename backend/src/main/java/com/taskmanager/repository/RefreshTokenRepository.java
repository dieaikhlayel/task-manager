package com.taskmanager.repository;

import com.taskmanager.model.RefreshToken;
import com.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    
    Optional<RefreshToken> findByUser(User user);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken rt WHERE rt.user = :user")
    void deleteByUser(@Param("user") User user);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate < :now")
    void deleteAllExpiredSince(@Param("now") Instant now);
    
    boolean existsByToken(String token);
    
    @Query("SELECT COUNT(rt) FROM RefreshToken rt WHERE rt.user = :user")
    long countByUser(@Param("user") User user);
    
    @Modifying
    @Transactional
    @Query("UPDATE RefreshToken rt SET rt.token = :newToken, rt.expiryDate = :expiryDate WHERE rt.user = :user")
    int updateTokenByUser(@Param("user") User user, 
                          @Param("newToken") String newToken, 
                          @Param("expiryDate") Instant expiryDate);
}
