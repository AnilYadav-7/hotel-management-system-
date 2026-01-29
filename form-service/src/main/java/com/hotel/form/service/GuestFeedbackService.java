package com.hotel.form.service;

import com.hotel.form.dto.GuestFeedbackDTO;
import com.hotel.form.entity.GuestFeedback;
import com.hotel.form.exception.GuestFeedbackException;
import com.hotel.form.repository.GuestFeedbackRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class GuestFeedbackService {

    private final GuestFeedbackRepository feedbackRepository;
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public GuestFeedbackService(GuestFeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public GuestFeedbackDTO submitFeedback(GuestFeedbackDTO feedbackDTO) {
        try {
            log.info("Attempting to save guest feedback from: {}", feedbackDTO.getGuestName());

            GuestFeedback feedback = GuestFeedback.builder()
                    .guestName(feedbackDTO.getGuestName().trim())
                    .guestEmail(feedbackDTO.getGuestEmail().trim())
                    .roomNumber(feedbackDTO.getRoomNumber() != null ? feedbackDTO.getRoomNumber().trim() : null)
                    .rating(feedbackDTO.getRating())
                    .serviceQuality(feedbackDTO.getServiceQuality().trim())
                    .roomCleanliness(feedbackDTO.getRoomCleanliness().trim())
                    .amenitiesFeedback(feedbackDTO.getAmenitiesFeedback().trim())
                    .overallExperience(feedbackDTO.getOverallExperience().trim())
                    .suggestions(feedbackDTO.getSuggestions() != null ? feedbackDTO.getSuggestions().trim() : null)
                    .build();

            GuestFeedback savedFeedback = feedbackRepository.save(feedback);

            log.info("Successfully saved guest feedback with ID: {} from guest: {}", 
                    savedFeedback.getFeedbackId(), savedFeedback.getGuestName());

            return convertEntityToDTO(savedFeedback);

        } catch (Exception ex) {
            log.error("Error saving guest feedback: {}", ex.getMessage(), ex);
            throw new GuestFeedbackException("SAVE_ERROR", 
                    "Failed to save guest feedback: " + ex.getMessage(), ex);
        }
    }

    @Transactional(readOnly = true)
    public List<GuestFeedbackDTO> getAllFeedbacks(int pageNo, int pageSize) {
        try {
            log.info("Fetching all guest feedbacks - page: {}, size: {}", pageNo, pageSize);
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<GuestFeedback> feedbackPage = feedbackRepository.findAll(pageable);
            log.info("Found {} feedbacks", feedbackPage.getTotalElements());
            return feedbackPage.getContent().stream()
                    .map(this::convertEntityToDTO)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Error fetching all feedbacks: {}", ex.getMessage(), ex);
            throw new GuestFeedbackException("FETCH_ERROR", 
                    "Failed to fetch all feedbacks: " + ex.getMessage(), ex);
        }
    }

    @Transactional(readOnly = true)
    public GuestFeedbackDTO getFeedbackById(Long id) {
        try {
            log.info("Fetching feedback with ID: {}", id);
            GuestFeedback feedback = feedbackRepository.findById(id)
                    .orElseThrow(() -> new GuestFeedbackException("NOT_FOUND", 
                            "Feedback with ID " + id + " not found"));
            return convertEntityToDTO(feedback);
        } catch (GuestFeedbackException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error fetching feedback by ID {}: {}", id, ex.getMessage(), ex);
            throw new GuestFeedbackException("FETCH_ERROR", 
                    "Failed to fetch feedback: " + ex.getMessage(), ex);
        }
    }

    public GuestFeedbackDTO updateFeedback(Long id, GuestFeedbackDTO feedbackDTO) {
        try {
            log.info("Updating feedback with ID: {}", id);
            
            GuestFeedback existingFeedback = feedbackRepository.findById(id)
                    .orElseThrow(() -> new GuestFeedbackException("NOT_FOUND", 
                            "Feedback with ID " + id + " not found"));

            existingFeedback.setGuestName(feedbackDTO.getGuestName().trim());
            existingFeedback.setGuestEmail(feedbackDTO.getGuestEmail().trim());
            existingFeedback.setRoomNumber(feedbackDTO.getRoomNumber() != null ? feedbackDTO.getRoomNumber().trim() : null);
            existingFeedback.setRating(feedbackDTO.getRating());
            existingFeedback.setServiceQuality(feedbackDTO.getServiceQuality().trim());
            existingFeedback.setRoomCleanliness(feedbackDTO.getRoomCleanliness().trim());
            existingFeedback.setAmenitiesFeedback(feedbackDTO.getAmenitiesFeedback().trim());
            existingFeedback.setOverallExperience(feedbackDTO.getOverallExperience().trim());
            existingFeedback.setSuggestions(feedbackDTO.getSuggestions() != null ? feedbackDTO.getSuggestions().trim() : null);

            GuestFeedback updatedFeedback = feedbackRepository.save(existingFeedback);
            log.info("Successfully updated feedback with ID: {}", id);
            
            return convertEntityToDTO(updatedFeedback);
        } catch (GuestFeedbackException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error updating feedback with ID {}: {}", id, ex.getMessage(), ex);
            throw new GuestFeedbackException("UPDATE_ERROR", 
                    "Failed to update feedback: " + ex.getMessage(), ex);
        }
    }

    public void deleteFeedback(Long id) {
        try {
            log.info("Deleting feedback with ID: {}", id);
            
            if (!feedbackRepository.existsById(id)) {
                throw new GuestFeedbackException("NOT_FOUND", 
                        "Feedback with ID " + id + " not found");
            }
            
            feedbackRepository.deleteById(id);
            log.info("Successfully deleted feedback with ID: {}", id);
        } catch (GuestFeedbackException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error deleting feedback with ID {}: {}", id, ex.getMessage(), ex);
            throw new GuestFeedbackException("DELETE_ERROR", 
                    "Failed to delete feedback: " + ex.getMessage(), ex);
        }
    }

    @Transactional(readOnly = true)
    public List<GuestFeedbackDTO> searchAndFilter(String name, Long id, 
                                                    String roomNumber,
                                                    Integer minRating, Integer maxRating) {
        try {
            log.info("Searching and filtering feedbacks - name: {}, id: {}, roomNumber: {}, minRating: {}, maxRating: {}", 
                    name, id, roomNumber, minRating, maxRating);
            
            List<GuestFeedback> results;
            
            if (id != null) {
                GuestFeedback feedback = feedbackRepository.findById(id).orElse(null);
                results = feedback != null ? List.of(feedback) : List.of();
            } else if (name != null && !name.trim().isEmpty() && 
                     roomNumber != null && !roomNumber.trim().isEmpty() && 
                     minRating != null && maxRating != null) {
                results = feedbackRepository.findByNameRoomAndRating(
                        name.trim(), roomNumber.trim(), minRating, maxRating);
            } else if (name != null && !name.trim().isEmpty() && 
                     minRating != null && maxRating != null) {
                results = feedbackRepository.findByNameAndRatingRange(
                        name.trim(), minRating, maxRating);
            } else if (name != null && !name.trim().isEmpty() && 
                     roomNumber != null && !roomNumber.trim().isEmpty()) {
                results = feedbackRepository.findByNameAndRoomNumber(name.trim(), roomNumber.trim());
            } else if (roomNumber != null && !roomNumber.trim().isEmpty() && 
                     minRating != null && maxRating != null) {
                results = feedbackRepository.findByRoomNumberAndRatingRange(
                        roomNumber.trim(), minRating, maxRating);
            } else if (name != null && !name.trim().isEmpty()) {
                results = feedbackRepository.findByGuestNameContainingIgnoreCase(name.trim());
            } else if (roomNumber != null && !roomNumber.trim().isEmpty()) {
                results = feedbackRepository.findByRoomNumber(roomNumber.trim());
            } else if (minRating != null && maxRating != null) {
                results = feedbackRepository.findByRatingBetween(minRating, maxRating);
            } else if (minRating != null) {
                results = feedbackRepository.findByRatingGreaterThanEqual(minRating);
            } else if (maxRating != null) {
                results = feedbackRepository.findByRatingLessThanEqual(maxRating);
            } else {
                results = feedbackRepository.findAll();
            }
            
            log.info("Found {} matching feedbacks", results.size());
            return results.stream()
                    .map(this::convertEntityToDTO)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Error searching/filtering feedbacks: {}", ex.getMessage(), ex);
            throw new GuestFeedbackException("SEARCH_ERROR", 
                    "Failed to search/filter feedbacks: " + ex.getMessage(), ex);
        }
    }

    private GuestFeedbackDTO convertEntityToDTO(GuestFeedback feedback) {
        String formattedCreatedAt = feedback.getCreatedAt() != null 
            ? feedback.getCreatedAt().format(DATE_FORMATTER) 
            : null;

        return GuestFeedbackDTO.builder()
                .feedbackId(feedback.getFeedbackId())
                .guestName(feedback.getGuestName())
                .guestEmail(feedback.getGuestEmail())
                .roomNumber(feedback.getRoomNumber())
                .rating(feedback.getRating())
                .serviceQuality(feedback.getServiceQuality())
                .roomCleanliness(feedback.getRoomCleanliness())
                .amenitiesFeedback(feedback.getAmenitiesFeedback())
                .overallExperience(feedback.getOverallExperience())
                .suggestions(feedback.getSuggestions())
                .createdAt(formattedCreatedAt)
                .build();
    }
}
