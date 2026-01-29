package com.hotel.bookings.dto;

import com.hotel.bookings.model.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingResponseDTO {
    private Long id;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private Double totalAmount;
    private BookingStatus status;
    private GuestResponseDTO guest;
    private RoomResponseDTO room; // Room info from rooms-service
}
