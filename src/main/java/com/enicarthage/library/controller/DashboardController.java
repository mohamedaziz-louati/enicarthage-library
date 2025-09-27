package com.enicarthage.library.controller;

import com.enicarthage.library.repository.BookRepository;
import com.enicarthage.library.repository.EventRepository;
import com.enicarthage.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
public class DashboardController {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getDashboardOverview() {
        Map<String, Object> overview = new HashMap<>();
        
        try {
            // Book statistics
            Map<String, Object> bookStats = new HashMap<>();
            bookStats.put("totalBooks", bookRepository.count());
            bookStats.put("availableBooks", bookRepository.findByStatus(com.enicarthage.library.entity.Book.BookStatus.AVAILABLE).size());
            
            // User statistics
            Map<String, Object> userStats = new HashMap<>();
            userStats.put("totalUsers", userRepository.count());
            userStats.put("students", userRepository.findByRole(com.enicarthage.library.entity.User.Role.STUDENT).size());
            userStats.put("faculty", userRepository.findByRole(com.enicarthage.library.entity.User.Role.FACULTY).size());
            userStats.put("librarians", userRepository.findByRole(com.enicarthage.library.entity.User.Role.LIBRARIAN).size());
            userStats.put("admins", userRepository.findByRole(com.enicarthage.library.entity.User.Role.ADMIN).size());
            
            // Event statistics
            Map<String, Object> eventStats = new HashMap<>();
            eventStats.put("totalEvents", eventRepository.count());
            eventStats.put("upcomingEvents", eventRepository.findByStartDateAfterOrderByStartDateAsc(LocalDateTime.now()).size());
            eventStats.put("ongoingEvents", eventRepository.findOngoingEvents(LocalDateTime.now()).size());
            
            overview.put("books", bookStats);
            overview.put("users", userStats);
            overview.put("events", eventStats);
            overview.put("lastUpdated", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            return ResponseEntity.ok(overview);
        } catch (Exception e) {
            System.out.println("ERROR: Failed to get dashboard overview: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to load dashboard data: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
