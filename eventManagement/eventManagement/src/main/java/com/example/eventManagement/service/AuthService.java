package com.example.eventManagement.service;

import com.example.eventManagement.model.Token;
import com.example.eventManagement.model.User;
import com.example.eventManagement.dao.*;
import com.example.eventManagement.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public boolean sendOtp(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) return false;

        User user = optionalUser.get();

        // Generate 6-digit OTP
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        // Save token to DB
        Token token = new Token();
        token.setEmail(email);
        token.setOtp(otp);
        token.setCreatedAt(LocalDateTime.now());
        tokenRepository.save(token);

        // Send OTP via email
        emailService.sendOtpEmail(email, otp);

        return true;
    }

    public String verifyOtp(String email, String otp) {
        Token token = tokenRepository.findTopByEmailOrderByIdDesc(email);
        if (token != null && token.getOtp().equals(otp)) {
            return jwtTokenUtil.generateToken(email);
        }
        return null;
    }
}
