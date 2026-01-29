package com.hotel.auth.dto;

import com.hotel.common.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String username;
    private Role role;
}
