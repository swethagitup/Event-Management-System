package com.example.eventManagement.service;

import com.example.eventManagement.model.Event;
import com.example.eventManagement.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    // Admin: Get all events
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // User: View only future and ongoing events
    public List<Event> getActiveEvents() {
        LocalDate today = LocalDate.now();
        return eventRepository.findByDateGreaterThanEqual(today);
    }

    // User: View completed events (optional)
    public List<Event> getCompletedEvents() {
        LocalDate today = LocalDate.now();
        return eventRepository.findByDateBefore(today);
    }

    // Manager: Add event
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    // Manager: Update event
    public Optional<Event> updateEvent(Long id, Event newEventData) {
        return eventRepository.findById(id).map(event -> {
        	event.setName(newEventData.getName());
        	event.setDescription(newEventData.getDescription());
        	event.setDate(newEventData.getDate());
        	event.setLocation(newEventData.getLocation());
        	event.setStatus(newEventData.getStatus());
        	event.setTitle(newEventData.getTitle());

            return eventRepository.save(event);
        });
    }

    // Manager: Delete event
    public boolean deleteEvent(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Optional: Get single event by ID
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }
}
