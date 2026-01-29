package com.hotel.bookings.dto;

import lombok.Data;

@Data
public class GuestResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String idProofNumber;
    private String address;
}
