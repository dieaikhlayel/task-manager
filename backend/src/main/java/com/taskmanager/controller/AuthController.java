
package com.taskmanager.controller;

import com.taskmanager.dto.*;
import com.taskmanager.service.UserService;
import com.taskmanager.util.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Generate JWT token
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtils.generateToken(userDetails.getUsername());
            
            // Get user details
            UserDTO userDTO = userService.getUserByUsername(userDetails.getUsername());
            
            // Create response
            JwtResponse response = new JwtResponse();
            response.setToken(jwt);
            response.setType("Bearer");
            response.setId(userDTO.getId());
            response.setUsername(userDTO.getUsername());
            response.setEmail(userDTO.getEmail());
            response.setRoles(userDTO.getRoles());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Invalid username or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Check if passwords match
            if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
                return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Passwords do not match"));
            }

            // Register user
            UserDTO registeredUser = userService.registerUser(registerRequest);
            
            // Auto-login after registration (generate token)
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    registerRequest.getUsername(),
                    registerRequest.getPassword()
                )
            );

            String jwt = jwtUtils.generateToken(registeredUser.getUsername());
            
            JwtResponse response = new JwtResponse();
            response.setToken(jwt);
            response.setType("Bearer");
            response.setId(registeredUser.getId());
            response.setUsername(registeredUser.getUsername());
            response.setEmail(registeredUser.getEmail());
            response.setRoles(registeredUser.getRoles());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // In JWT, logout is handled client-side by removing the token
        // But we can add token to blacklist if implementing that feature
        return ResponseEntity.ok(new MessageResponse("Logged out successfully"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            String username = jwtUtils.getUsernameFromToken(token.substring(7));
            UserDTO userDTO = userService.getUserByUsername(username);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Invalid token"));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token) {
        try {
            String oldToken = token.substring(7);
            String username = jwtUtils.getUsernameFromToken(oldToken);
            
            if (jwtUtils.validateToken(oldToken, username)) {
                String newToken = jwtUtils.generateToken(username);
                return ResponseEntity.ok(new RefreshTokenResponse(newToken));
            } else {
                return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid token"));
            }
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Token refresh failed"));
        }
    }
}
