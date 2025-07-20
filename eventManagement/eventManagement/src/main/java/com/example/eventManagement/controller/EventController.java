package com.example.eventManagement.controller;

import com.example.eventManagement.model.*;
import com.example.eventManagement.dao.*;
import com.example.eventManagement.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TokenService tokenService;

    // ✅ Create Event (Admin only)
    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody Event event,
                                         @RequestHeader("Authorization") String tokenHeader) {

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Authorization header missing or invalid");
        }

        String token = tokenHeader.substring(7); // Remove "Bearer "
        String role = tokenService.getRoleFromToken(token);

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body("Only admin can create events");
        }

        return ResponseEntity.ok(eventRepository.save(event));
    }

    // ✅ View All Events (for manager, user, admin)
    @GetMapping
    public ResponseEntity<?> getAllEvents(@RequestHeader("Authorization") String tokenHeader) {

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Authorization header missing");
        }

        String token = tokenHeader.substring(7); // Remove "Bearer "
        if (!tokenService.isValid(token)) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        List<Event> events = eventRepository.findAll();
        return ResponseEntity.ok(events);
    }

    // Add more endpoints as needed (assign user, update event, delete event, etc.)
}
