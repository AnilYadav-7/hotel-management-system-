package com.hotel.rooms.dto;

import com.hotel.rooms.model.RoomStatus;
import com.hotel.rooms.model.RoomType;
import lombok.Data;

@Data
public class RoomResponseDTO {
    private Long id;
    private String roomNumber;
    private RoomType roomType;
    private Double price;
    private RoomStatus status;
}
