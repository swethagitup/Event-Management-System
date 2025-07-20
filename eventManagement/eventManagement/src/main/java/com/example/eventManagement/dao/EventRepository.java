package com.example.eventManagement.dao;

import com.example.eventManagement.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStatus(String status);

    // ✅ Fixed methods
    List<Event> findByDateGreaterThanEqual(LocalDate date);
    List<Event> findByDateBefore(LocalDate date);
}
