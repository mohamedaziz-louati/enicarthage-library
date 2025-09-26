package com.enicarthage.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class BasicController {
    
    @GetMapping("/test")
    public String test() {
        return "Basic controller is working!";
    }
    
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");
            
            // Simple hardcoded authentication for testing
            if (("admin".equals(username) && "admin123".equals(password)) ||
                ("student".equals(username) && "student123".equals(password)) ||
                ("librarian".equals(username) && "librarian123".equals(password))) {
                
                // Create response
                Map<String, Object> response = new HashMap<>();
                response.put("token", "simple-token-" + System.currentTimeMillis());
                response.put("type", "Bearer");
                response.put("id", 1L);
                response.put("username", username);
                response.put("email", username + "@enicarthage.tn");
                response.put("firstName", username.substring(0, 1).toUpperCase() + username.substring(1));
                response.put("lastName", "User");
                response.put("role", username.equals("admin") ? "ADMIN" : username.equals("librarian") ? "LIBRARIAN" : "STUDENT");
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid username or password");
                return ResponseEntity.badRequest().body(error);
            }
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Login failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> userData) {
        try {
            String username = userData.get("username");
            
            // For now, just return success (in real implementation, we'd save to database)
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("userId", System.currentTimeMillis());
            response.put("username", username);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
