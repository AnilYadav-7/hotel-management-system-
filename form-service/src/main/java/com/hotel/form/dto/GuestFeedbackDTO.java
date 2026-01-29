package com.hotel.form.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestFeedbackDTO {
    private Long feedbackId;

    @NotBlank(message = "Guest name is required")
    @Size(min = 3, max = 100, message = "Guest name must be between 3 and 100 characters")
    private String guestName;

    @NotBlank(message = "Guest email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String guestEmail;

    @Size(max = 20, message = "Room number must not exceed 20 characters")
    private String roomNumber;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @NotBlank(message = "Service quality feedback is required")
    @Size(min = 10, max = 1000, message = "Service quality feedback must be between 10 and 1000 characters")
    private String serviceQuality;

    @NotBlank(message = "Room cleanliness feedback is required")
    @Size(min = 10, max = 1000, message = "Room cleanliness feedback must be between 10 and 1000 characters")
    private String roomCleanliness;

    @NotBlank(message = "Amenities feedback is required")
    @Size(min = 10, max = 1000, message = "Amenities feedback must be between 10 and 1000 characters")
    private String amenitiesFeedback;

    @NotBlank(message = "Overall experience feedback is required")
    @Size(min = 10, max = 1000, message = "Overall experience feedback must be between 10 and 1000 characters")
    private String overallExperience;

    @Size(max = 2000, message = "Suggestions must not exceed 2000 characters")
    private String suggestions;

    private String createdAt;
}
