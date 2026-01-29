package com.hotel.form.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "guest_feedbacks", indexes = {
    @Index(name = "idx_guest_name", columnList = "guest_name"),
    @Index(name = "idx_room_number", columnList = "room_number"),
    @Index(name = "idx_rating", columnList = "rating"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long feedbackId;

    @Column(name = "guest_name", nullable = false, length = 100)
    private String guestName;

    @Column(name = "guest_email", nullable = false, length = 100)
    private String guestEmail;

    @Column(name = "room_number", length = 20)
    private String roomNumber;

    @Column(name = "rating", nullable = false)
    private Integer rating; // 1-5 stars

    @Column(name = "service_quality", nullable = false, columnDefinition = "LONGTEXT")
    private String serviceQuality;

    @Column(name = "room_cleanliness", nullable = false, columnDefinition = "LONGTEXT")
    private String roomCleanliness;

    @Column(name = "amenities_feedback", nullable = false, columnDefinition = "LONGTEXT")
    private String amenitiesFeedback;

    @Column(name = "overall_experience", nullable = false, columnDefinition = "LONGTEXT")
    private String overallExperience;

    @Column(name = "suggestions", columnDefinition = "LONGTEXT")
    private String suggestions;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
