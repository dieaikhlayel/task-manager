package com.taskmanager.service;

import com.taskmanager.dto.UserDTO;
import com.taskmanager.dto.RegisterRequest;
import com.taskmanager.dto.UpdateProfileRequest;

import java.util.List;

public interface UserService {
    UserDTO registerUser(RegisterRequest request);
    UserDTO getUserById(Long id);
    UserDTO getUserByUsername(String username);
    UserDTO getUserByEmail(String email);
    UserDTO updateUser(Long id, UpdateProfileRequest request);
    void deleteUser(Long id);
    List<UserDTO> getAllUsers();
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    UserDTO updateLastLogin(String username);
    void verifyEmail(Long id);
    List<UserDTO> searchUsers(String keyword);
    long getUserCount();
}
