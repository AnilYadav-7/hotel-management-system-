package com.hotel.guests.dto;

import lombok.Data;

@Data
public class GuestResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String idProofNumber;
    private String address;
}
