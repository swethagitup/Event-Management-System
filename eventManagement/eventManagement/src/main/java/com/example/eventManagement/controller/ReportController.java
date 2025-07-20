package com.example.eventManagement.controller;

import com.example.eventManagement.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReportController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    @GetMapping("/reports")
    public ResponseEntity<Map<String, Integer>> getReports() {
        int totalUsers = (int) userRepository.count();
        int totalEvents = (int) eventRepository.count();
        int totalParticipation = (int) participationRepository.count(); // If you have it

        Map<String, Integer> report = new HashMap<>();
        report.put("totalUsers", totalUsers);
        report.put("totalEvents", totalEvents);
        report.put("totalParticipation", totalParticipation);

        return ResponseEntity.ok(report);
    }
}
