package com.example.eventManagement.service;

import com.example.eventManagement.model.Token;
import com.example.eventManagement.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenService {

    private static final int OTP_EXPIRY_MINUTES = 5;

    @Autowired
    private TokenRepository tokenRepository;

    public void saveToken(String email, String otp, String role) {
        Token token = new Token();
        token.setEmail(email);
        token.setOtp(otp);
        token.setRole(role);  // ✅ Set role here
        token.setCreatedAt(LocalDateTime.now());
        Token savedToken = tokenRepository.save(token);
        Long tokenId = savedToken.getId(); 
    }
    
    public String getRoleFromToken(String token) {
        try {
            Long tokenId = Long.parseLong(token);  // Assuming the token is the ID
            Token t = tokenRepository.findById(tokenId).orElse(null);
            return t != null ? t.getRole() : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public boolean isValid(String token) {
        try {
            Long tokenId = Long.parseLong(token);
            return tokenRepository.existsById(tokenId);
        } catch (NumberFormatException e) {
            return false;
        }
    }



    public boolean validateOtp(String email, String inputOtp) {
        Token token = tokenRepository.findTopByEmailOrderByIdDesc(email);
        if (token == null) {
            return false;
        }

        // Optional expiry check
        LocalDateTime now = LocalDateTime.now();
        if (token.getCreatedAt().plusMinutes(OTP_EXPIRY_MINUTES).isBefore(now)) {
            return false; // OTP expired
        }

        return token.getOtp().equals(inputOtp);
    }
}
