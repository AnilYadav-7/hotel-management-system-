package com.hotel.guests.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class GuestRequestDTO {
    @NotBlank(message = "Guest full name is required")
    private String name;

    @NotBlank(message = "Guest email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Guest phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotBlank(message = "ID proof number is required")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,20}$", message = "ID proof number must be alphanumeric and 5-20 characters long")
    private String idProofNumber;

    @NotBlank(message = "Guest address is required")
    private String address;
}
