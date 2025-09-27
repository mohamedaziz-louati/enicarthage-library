package com.enicarthage.library.controller;

import com.enicarthage.library.entity.User;
import com.enicarthage.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status) {
        
        try {
            System.out.println("=== getAllUsers Called ===");
            System.out.println("Page: " + page);
            System.out.println("Size: " + size);
            System.out.println("SortBy: " + sortBy);
            System.out.println("SortDir: " + sortDir);
            System.out.println("Search: " + search);
            System.out.println("Role: " + role);
            System.out.println("Status: " + status);
            
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            
            Page<User> usersPage;
            
            if (search != null && !search.trim().isEmpty()) {
                usersPage = userRepository.findBySearchTerm(search, pageable);
            } else if (role != null && !role.trim().isEmpty()) {
                usersPage = userRepository.findByRole(role, pageable);
            } else if (status != null && !status.trim().isEmpty()) {
                usersPage = userRepository.findByStatus(status, pageable);
            } else {
                usersPage = userRepository.findAll(pageable);
            }
            
            // Remove passwords from response
            usersPage.getContent().forEach(user -> user.setPassword(null));
            
            Map<String, Object> response = new HashMap<>();
            response.put("content", usersPage.getContent());
            response.put("totalElements", usersPage.getTotalElements());
            response.put("totalPages", usersPage.getTotalPages());
            response.put("number", usersPage.getNumber());
            response.put("size", usersPage.getSize());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("ERROR: Failed to retrieve users: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> error = new HashMap<>();
            
            // Handle validation errors specifically
            if (e.getMessage().contains("Validation failed")) {
                error.put("error", "Validation failed: Please check all fields are correctly formatted. Email must be a valid email address.");
            } else if (e.getMessage().contains("already exists")) {
                error.put("error", "User with this username or email already exists");
            } else {
                error.put("error", "Failed to retrieve users: " + e.getMessage());
            }
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            System.out.println("=== Creating User ===");
            System.out.println("Username: " + user.getUsername());
            System.out.println("Email: " + user.getEmail());
            System.out.println("First Name: " + user.getFirstName());
            System.out.println("Last Name: " + user.getLastName());
            System.out.println("Role: " + user.getRole());
            
            // Check if username already exists
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Username already exists");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Check if email already exists
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Email already exists");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Encode password
            if (user.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            
            // Set default values
            if (user.getStatus() == null) {
                user.setStatus(User.UserStatus.ACTIVE);
            }
            
            if (user.getCreatedAt() == null) {
                user.setCreatedAt(LocalDateTime.now());
            }
            
            User savedUser = userRepository.save(user);
            System.out.println("SUCCESS: User created with ID: " + savedUser.getId());
            
            // Remove password from response
            savedUser.setPassword(null);
            
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create user: " + e.getMessage());
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(u -> {
            u.setPassword(null); // Remove password from response
            return ResponseEntity.ok(u);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            return userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(userDetails.getFirstName());
                    user.setLastName(userDetails.getLastName());
                    user.setEmail(userDetails.getEmail());
                    user.setPhoneNumber(userDetails.getPhoneNumber());
                    user.setRole(userDetails.getRole());
                    user.setStatus(userDetails.getStatus());
                    
                    // Update password if provided
                    if (userDetails.getPassword() != null && !userDetails.getPassword().trim().isEmpty()) {
                        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
                    }
                    
                    User updatedUser = userRepository.save(user);
                    updatedUser.setPassword(null); // Remove password from response
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update user: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestParam String role) {
        try {
            return userRepository.findById(id)
                .map(user -> {
                    user.setRole(User.Role.valueOf(role));
                    User updatedUser = userRepository.save(user);
                    updatedUser.setPassword(null); // Remove password from response
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update user role: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            return userRepository.findById(id)
                .map(user -> {
                    user.setStatus(User.UserStatus.valueOf(status));
                    User updatedUser = userRepository.save(user);
                    updatedUser.setPassword(null); // Remove password from response
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update user status: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "User deleted successfully");
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete user: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile() {
        // This would typically get the current logged-in user
        // For now, we'll return a simple implementation
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            User user = users.get(0);
            user.setPassword(null);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
        List<User> users = userRepository.findByRole(User.Role.valueOf(role));
        users.forEach(user -> user.setPassword(null)); // Remove passwords
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String name) {
        List<User> users = userRepository.findBySearchTerm(name);
        users.forEach(user -> user.setPassword(null)); // Remove passwords
        return ResponseEntity.ok(users);
    }
}
