package com.example.eventManagement.service;

import com.example.eventManagement.model.User;
import com.example.eventManagement.model.Role;
import com.example.eventManagement.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Register a new user
    public User registerUser(User user) {
        user.setRole("USER");
        return userRepository.save(user);
    }

    // Check if email exists (for OTP login)
    public boolean isRegisteredEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    // Get user by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // Get user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Get all users (Admin or Manager)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete user (Manager)
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Update user profile (Optional)
    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setPhone(updatedUser.getPhone());
            user.setGender(updatedUser.getGender());
            user.setAge(updatedUser.getAge());
            return userRepository.save(user);
        });
    }
}
