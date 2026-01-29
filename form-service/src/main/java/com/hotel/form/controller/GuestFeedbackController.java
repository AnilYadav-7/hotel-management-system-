package com.hotel.form.controller;

import com.hotel.form.dto.GuestFeedbackDTO;
import com.hotel.form.exception.GuestFeedbackException;
import com.hotel.form.service.GuestFeedbackService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedbacks")
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000"},
             allowedHeaders = "*", 
             methods = {org.springframework.web.bind.annotation.RequestMethod.GET, 
                       org.springframework.web.bind.annotation.RequestMethod.POST, 
                       org.springframework.web.bind.annotation.RequestMethod.PUT, 
                       org.springframework.web.bind.annotation.RequestMethod.DELETE, 
                       org.springframework.web.bind.annotation.RequestMethod.OPTIONS})
public class GuestFeedbackController {

    private final GuestFeedbackService feedbackService;

    public GuestFeedbackController(GuestFeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitFeedback(
            @Valid @RequestBody GuestFeedbackDTO feedbackDTO) {

        log.info("Received feedback submission from guest: {}", feedbackDTO.getGuestName());

        GuestFeedbackDTO savedFeedback = feedbackService.submitFeedback(feedbackDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Feedback submitted successfully");
        response.put("data", savedFeedback);

        log.info("Feedback submitted successfully with ID: {}", savedFeedback.getFeedbackId());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'RECEPTIONIST')")
    public ResponseEntity<Map<String, Object>> getAllFeedbacks(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("Received request to fetch all feedbacks - page: {}, size: {}", pageNo, pageSize);

        List<GuestFeedbackDTO> feedbacks = feedbackService.getAllFeedbacks(pageNo, pageSize);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Feedbacks retrieved successfully");
        response.put("data", feedbacks);
        response.put("count", feedbacks.size());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'RECEPTIONIST')")
    public ResponseEntity<Map<String, Object>> getFeedbackById(@PathVariable Long id) {
        log.info("Received request to fetch feedback with ID: {}", id);

        GuestFeedbackDTO feedback = feedbackService.getFeedbackById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Feedback retrieved successfully");
        response.put("data", feedback);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('MANAGER', 'RECEPTIONIST')")
    public ResponseEntity<Map<String, Object>> searchAndFilterFeedbacks(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String roomNumber,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) Integer maxRating) {

        log.info("Received search/filter request - name: {}, id: {}, roomNumber: {}, minRating: {}, maxRating: {}", 
                name, id, roomNumber, minRating, maxRating);

        List<GuestFeedbackDTO> feedbacks = feedbackService.searchAndFilter(
                name, id, roomNumber, minRating, maxRating);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Search completed successfully");
        response.put("data", feedbacks);
        response.put("count", feedbacks.size());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'RECEPTIONIST')")
    public ResponseEntity<Map<String, Object>> updateFeedback(
            @PathVariable Long id,
            @Valid @RequestBody GuestFeedbackDTO feedbackDTO) {

        log.info("Received request to update feedback with ID: {}", id);

        GuestFeedbackDTO updatedFeedback = feedbackService.updateFeedback(id, feedbackDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Feedback updated successfully");
        response.put("data", updatedFeedback);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Map<String, Object>> deleteFeedback(@PathVariable Long id) {
        log.info("Received request to delete feedback with ID: {}", id);

        feedbackService.deleteFeedback(id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Feedback deleted successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Guest Feedback Service API is running");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
