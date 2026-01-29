package com.hotel.guests.service;

import com.hotel.common.exception.ResourceNotFoundException;
import com.hotel.guests.dto.GuestRequestDTO;
import com.hotel.guests.dto.GuestResponseDTO;

import java.util.List;

public interface GuestService {
    GuestResponseDTO addGuest(GuestRequestDTO request) throws ResourceNotFoundException;
    GuestResponseDTO getGuestById(Long id) throws ResourceNotFoundException;
    GuestResponseDTO getGuestByPhone(String phone) throws ResourceNotFoundException;
    GuestResponseDTO updateGuest(Long id, GuestRequestDTO request) throws ResourceNotFoundException;
    void deleteGuest(Long id) throws ResourceNotFoundException;
    List<GuestResponseDTO> getAllGuests(int pageNo, int pageSize) throws ResourceNotFoundException;
}
