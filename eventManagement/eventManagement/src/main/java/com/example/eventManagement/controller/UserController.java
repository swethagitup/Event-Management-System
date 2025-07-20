package com.example.eventManagement.controller;

import com.example.eventManagement.model.User;
import com.example.eventManagement.model.Event;
import com.example.eventManagement.dao.*;
import com.example.eventManagement.service.EmailService;
import com.example.eventManagement.service.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:9000")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private TokenService tokenService;

    private Map<String, String> otpStorage = new HashMap<>();

    // ✅ User Registration
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return "User already registered";
        }
        userRepository.save(user);
        return "Registration successful";
    }

    // ✅ Login and Send OTP to email (if email exists)
    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String email, @RequestParam String password) {
        Optional<User> user = userRepository.findByEmailAndPassword(email, password);
        if (user.isPresent()) {
            String otp = String.valueOf(new Random().nextInt(900000) + 100000);
            otpStorage.put(email, otp);
            emailService.sendOtpEmail(email, otp);
            return "OTP sent to email";
        }
        return "Invalid email or password";
    }
    
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String tokenHeader) {
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing Authorization header");
        }

        String token = tokenHeader.substring(7);
        String role = tokenService.getRoleFromToken(token);

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body("Only ADMIN can view users");
        }

        return ResponseEntity.ok(userRepository.findAll());
    }

    // ✅ Verify OTP
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email, @RequestParam String otp) {
        if (otpStorage.containsKey(email) && otpStorage.get(email).equals(otp)) {
            otpStorage.remove(email);
            return "OTP verified. Access granted.";
        }
        return "Invalid OTP";
    }

    // ✅ Apply to event (only for UPCOMING or ONGOING)
    @PostMapping("/apply")
    public String applyToEvent(@RequestParam String email, @RequestParam Long eventId) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        Optional<Event> eventOpt = eventRepository.findById(eventId);

        if (userOpt.isPresent() && eventOpt.isPresent()) {
            Event event = eventOpt.get();
            if ("COMPLETED".equalsIgnoreCase(event.getStatus())) {
                return "Cannot apply to completed event.";
            }
            User user = userOpt.get();
            user.setEvent(event); // ✅
            user.setStatus("REGISTERED");
            userRepository.save(user);
            return "Applied to event successfully";
        }
        return "Invalid user or event";
    }


    // ✅ Mark not interested
    @PostMapping("/not-interested")
    public String markNotInterested(@RequestParam String email, @RequestParam Long eventId) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (userOpt.isPresent() && eventOpt.isPresent()) {
            User user = userOpt.get();
            user.setEvent(eventOpt.get()); // ✅
            user.setStatus("NOT_INTERESTED");
            userRepository.save(user);
            return "Marked as not interested";
        }
        return "User or Event not found";
    }

}
