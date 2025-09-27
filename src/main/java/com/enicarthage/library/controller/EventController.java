package com.enicarthage.library.controller;

import com.enicarthage.library.dto.EventCreateRequest;
import com.enicarthage.library.entity.Event;
import com.enicarthage.library.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status) {
        
        try {
            System.out.println("=== getAllEvents Called ===");
            System.out.println("Page: " + page);
            System.out.println("Size: " + size);
            System.out.println("SortBy: " + sortBy);
            System.out.println("SortDir: " + sortDir);
            System.out.println("Search: " + search);
            System.out.println("Status: " + status);
            
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            
            Page<Event> eventsPage;
            
            if (search != null && !search.trim().isEmpty()) {
                eventsPage = eventRepository.findBySearchTerm(search, pageable);
            } else if (status != null && !status.trim().isEmpty()) {
                eventsPage = eventRepository.findByStatus(status, pageable);
            } else {
                eventsPage = eventRepository.findAll(pageable);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("content", eventsPage.getContent());
            response.put("totalElements", eventsPage.getTotalElements());
            response.put("totalPages", eventsPage.getTotalPages());
            response.put("number", eventsPage.getNumber());
            response.put("size", eventsPage.getSize());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to retrieve events: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventCreateRequest request) {
        try {
            System.out.println("=== Creating Event ===");
            System.out.println("Title: " + request.getTitle());
            System.out.println("Description: " + request.getDescription());
            System.out.println("Start Date: " + request.getStartDateStr());
            System.out.println("End Date: " + request.getEndDateStr());
            System.out.println("Location: " + request.getLocation());
            System.out.println("Type: " + request.getType());
            
            // Create new Event entity
            Event event = new Event();
            event.setTitle(request.getTitle());
            event.setDescription(request.getDescription());
            event.setLocation(request.getLocation());
            event.setType(request.getType() != null ? com.enicarthage.library.entity.Event.EventType.valueOf(request.getType()) : com.enicarthage.library.entity.Event.EventType.WORKSHOP);
            event.setRegistrationRequired(request.getRegistrationRequired() != null ? request.getRegistrationRequired() : false);
            
            // Parse dates - handle both YYYY-MM-DD and YYYY-MM-DDTHH:mm:ss formats
            try {
                String startDateStr = request.getStartDateStr();
                if (startDateStr.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                    // YYYY-MM-DD format, add default time
                    startDateStr = startDateStr + "T10:00:00";
                }
                event.setStartDate(java.time.LocalDateTime.parse(startDateStr));
                System.out.println("Parsed start date: " + event.getStartDate());
            } catch (Exception e) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Invalid start date format. Use YYYY-MM-DD or YYYY-MM-DDTHH:mm:ss");
                return ResponseEntity.badRequest().body(error);
            }
            
            try {
                String endDateStr = request.getEndDateStr();
                if (endDateStr.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                    // YYYY-MM-DD format, add default time
                    endDateStr = endDateStr + "T12:00:00";
                }
                event.setEndDate(java.time.LocalDateTime.parse(endDateStr));
                System.out.println("Parsed end date: " + event.getEndDate());
            } catch (Exception e) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Invalid end date format. Use YYYY-MM-DD or YYYY-MM-DDTHH:mm:ss");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Validate required fields
            if (event.getTitle() == null || event.getTitle().trim().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Title is required");
                return ResponseEntity.badRequest().body(error);
            }
            
            if (event.getDescription() == null || event.getDescription().trim().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Description is required");
                return ResponseEntity.badRequest().body(error);
            }
            
            if (event.getStartDate() == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Start date is required");
                return ResponseEntity.badRequest().body(error);
            }
            
            if (event.getEndDate() == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "End date is required");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Set default values
            event.setStatus(com.enicarthage.library.entity.Event.EventStatus.UPCOMING);
            event.setCreatedAt(java.time.LocalDateTime.now());
            event.setCurrentAttendees(0);
            
            Event savedEvent = eventRepository.save(event);
            System.out.println("SUCCESS: Event created with ID: " + savedEvent.getId());
            
            return ResponseEntity.ok(savedEvent);
        } catch (Exception e) {
            System.out.println("ERROR: Failed to create event: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to create event: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> event = eventRepository.findById(id);
        return event.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        try {
            return eventRepository.findById(id)
                .map(event -> {
                    event.setTitle(eventDetails.getTitle());
                    event.setDescription(eventDetails.getDescription());
                    event.setStartDate(eventDetails.getStartDate());
                    event.setEndDate(eventDetails.getEndDate());
                    event.setLocation(eventDetails.getLocation());
                    event.setType(eventDetails.getType());
                    event.setMaxAttendees(eventDetails.getMaxAttendees());
                    event.setStatus(eventDetails.getStatus());
                    event.setUpdatedAt(LocalDateTime.now());
                    
                    Event updatedEvent = eventRepository.save(event);
                    return ResponseEntity.ok(updatedEvent);
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            System.out.println("ERROR: Failed to update event: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to update event: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        try {
            return eventRepository.findById(id)
                .map(event -> {
                    eventRepository.delete(event);
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Event deleted successfully");
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete event: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEvents(@RequestParam String query) {
        List<Event> events = eventRepository.findBySearchTerm(query);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Event>> getUpcomingEvents() {
        List<Event> events = eventRepository.findByStartDateAfterOrderByStartDateAsc(LocalDateTime.now());
        return ResponseEntity.ok(events);
    }
}
