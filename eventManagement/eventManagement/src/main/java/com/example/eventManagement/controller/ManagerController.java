package com.example.eventManagement.controller;

import com.example.eventManagement.model.Event;
import com.example.eventManagement.model.User;
import com.example.eventManagement.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/manager")
@CrossOrigin(origins = "*")
public class ManagerController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Get all events
    @GetMapping("/events")
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // ✅ Add a new event
    @PostMapping("/events")
    public String addEvent(@RequestBody Event event) {
        eventRepository.save(event);
        return "Event added successfully";
    }

    // ✅ Edit/update an event
    @PutMapping("/events/{id}")
    public String updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isPresent()) {
            Event event = eventOpt.get();
            event.setTitle(eventDetails.getTitle());
            event.setDate(eventDetails.getDate());
            event.setDescription(eventDetails.getDescription());
            event.setStatus(eventDetails.getStatus());
            eventRepository.save(event);
            return "Event updated successfully";
        }
        return "Event not found";
    }

    // ✅ Delete an event
    @DeleteMapping("/events/{id}")
    public String deleteEvent(@PathVariable Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return "Event deleted successfully";
        }
        return "Event not found";
    }

    // ✅ Get users registered for a specific event
    @GetMapping("/events/{eventId}/users")
    public List<User> getUsersForEvent(@PathVariable Long eventId) {
        return userRepository.findByEventId(eventId);
    }

    // ✅ Add user to an event (register manually)
    @PostMapping("/events/{eventId}/users")
    public String addUserToEvent(@PathVariable Long eventId, @RequestBody User user) {
    	Event event = eventRepository.findById(eventId).orElse(null);
    	user.setEvent(event);
        userRepository.save(user);
        return "User added to event";
    }

    // ✅ Update user in event
    @PutMapping("/users/{userId}")
    public String updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setAge(updatedUser.getAge());
            user.setGender(updatedUser.getGender());
            user.setPhone(updatedUser.getPhone());
            userRepository.save(user);
            return "User updated successfully";
        }
        return "User not found";
    }

    // ✅ Delete user from event
    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return "User deleted successfully";
        }
        return "User not found";
    }

    // ✅ Remove "Not Interested" users (custom logic)
    @DeleteMapping("/events/{eventId}/users/not-interested")
    public String removeNotInterestedUsers(@PathVariable Long eventId) {
        List<User> users = userRepository.findByEventIdAndStatus(eventId, "NOT_INTERESTED");
        userRepository.deleteAll(users);
        return "Not interested users removed from event";
    }
}
