package com.example.eventManagement.controller;

import com.example.eventManagement.model.*;
import com.example.eventManagement.dao.*;
import com.example.eventManagement.security.*;
import com.example.eventManagement.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // ✅ Register new user
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Email already registered";
        }
        if (user.getRole() == null) {
            user.setRole("USER"); // default role
        }
        userRepository.save(user);
        return "User registered successfully";
    }


    // ✅ Send OTP to user's email
 // ✅ Modified: sendOtp returns OTP directly in response
    @PostMapping("/send-otp")
    public String sendOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String role = body.get("role");

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "Email not found";
        }

        User user = userOpt.get();

        if (!user.getPassword().equals(password)) {
            return "Invalid password";
        }

        if (!user.getRole().equalsIgnoreCase(role)) {
            return "Role mismatch";
        }

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        Token token = new Token();
        token.setEmail(email);
        token.setOtp(otp);
        token.setExpiry(LocalDateTime.now().plusMinutes(5));
        tokenRepository.save(token);

        return otp; // returning OTP directly (not emailing it)
    }




    // ✅ Verify OTP and return token
    @PostMapping("/verify-otp")
    public Map<String, String> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        Token token = tokenRepository.findTopByEmailOrderByIdDesc(email);
        Map<String, String> response = new HashMap<>();

        if (token == null || !token.getOtp().equals(otp)) {
            response.put("error", "Invalid OTP");
            return response;
        }

        if (token.getExpiry().isBefore(LocalDateTime.now())) {
            response.put("error", "OTP expired");
            return response;
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            response.put("error", "User not found");
            return response;
        }

        User user = userOpt.get();
        String jwt = jwtTokenUtil.generateToken(email);

        response.put("token", jwt);
        response.put("role", user.getRole());
        response.put("name", email);
        return response;
    }


}
