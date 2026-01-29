package com.hotel.bookings.dto;

import lombok.Data;

@Data
public class RoomResponseDTO {
    private Long id;
    private String roomNumber;
    private String roomType;
    private Double price;
    private String status;
}
