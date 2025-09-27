package com.enicarthage.library.controller;

import com.enicarthage.library.entity.User;
import com.enicarthage.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/simple-auth")
public class SimpleAuthController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    public ResponseEntity<?> simpleLogin(@RequestBody Map<String, String> credentials) {
        try {
            System.out.println("=== Login Request Received ===");
            System.out.println("Username: " + credentials.get("username"));
            System.out.println("Password provided: " + (credentials.get("password") != null ? "YES" : "NO"));
            
            String username = credentials.get("username");
            String password = credentials.get("password");
            
            if (username == null || password == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Username and password are required");
                System.out.println("ERROR: Missing username or password");
                return ResponseEntity.badRequest().body(error);
            }
            
            Optional<User> userOpt = userRepository.findByUsername(username);
            
            if (userOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "User not found");
                System.out.println("ERROR: User not found - " + username);
                return ResponseEntity.badRequest().body(error);
            }
            
            User user = userOpt.get();
            System.out.println("User found: " + user.getUsername() + ", has password: " + (user.getPassword() != null));
            
            if (!passwordEncoder.matches(password, user.getPassword())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid password");
                System.out.println("ERROR: Invalid password for user - " + username);
                return ResponseEntity.badRequest().body(error);
            }
            
            // Create simple response
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", "simple-token-" + System.currentTimeMillis());
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("role", user.getRole());
            
            System.out.println("SUCCESS: Login successful for - " + username);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Login failed: " + e.getMessage());
            System.out.println("EXCEPTION: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            var users = userRepository.findAll();
            users.forEach(user -> user.setPassword(null)); // Remove passwords
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to get users: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/debug")
    public ResponseEntity<?> debug() {
        try {
            Map<String, Object> debug = new HashMap<>();
            debug.put("userCount", userRepository.count());
            debug.put("users", userRepository.findAll().stream().map(u -> {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("username", u.getUsername());
                userMap.put("email", u.getEmail());
                userMap.put("role", u.getRole());
                userMap.put("hasPassword", u.getPassword() != null);
                return userMap;
            }).toList());
            return ResponseEntity.ok(debug);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Debug failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
