package com.example.eventManagement.dao;

import com.example.eventManagement.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    int countByStatus(String status); // Optional
}
