package com.example.eventManagement.dao;

import com.example.eventManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Just method signatures — NO logic or variables
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String password);
    List<User> findByEventId(Long eventId);
    List<User> findByEventIdAndStatus(Long eventId, String status);
    int countByStatus(String status);
    Optional<User> findByEmailAndPasswordAndRole(String email, String password, String role);

}
