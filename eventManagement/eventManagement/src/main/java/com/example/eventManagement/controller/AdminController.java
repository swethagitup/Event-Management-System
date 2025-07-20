package com.example.eventManagement.controller;

import com.example.eventManagement.model.*;
import com.example.eventManagement.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*") // allow cross-origin requests for frontend use
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    // GET all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // GET all events
    @GetMapping("/events")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

}
