package com.hotel.auth.service;

import com.hotel.auth.dto.AuthResponseDTO;
import com.hotel.auth.dto.LoginRequestDTO;
import com.hotel.auth.dto.RegisterRequestDTO;
import com.hotel.auth.dto.StaffRequestDTO;
import com.hotel.auth.model.User;
import com.hotel.common.exception.UserAlreadyExistsException;
import com.hotel.common.exception.ResourceNotFoundException;
import jakarta.validation.Valid;

import java.util.List;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO registerRequestDTO) throws UserAlreadyExistsException;
    AuthResponseDTO login(LoginRequestDTO loginRequestDTO);
    User createStaff(@Valid StaffRequestDTO request) throws UserAlreadyExistsException;
    List<User> getAllStaff();
    User updateStaff(Long id, @Valid StaffRequestDTO request) throws ResourceNotFoundException, UserAlreadyExistsException;
    void deleteStaff(Long id) throws ResourceNotFoundException;
}
