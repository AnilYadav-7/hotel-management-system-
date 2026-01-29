package com.hotel.rooms.service;

import com.hotel.common.exception.ResourceNotFoundException;
import com.hotel.common.exception.RoomAlreadyExistsException;
import com.hotel.rooms.dto.RoomRequestDTO;
import com.hotel.rooms.dto.RoomResponseDTO;

import java.util.List;

public interface RoomsService {
    RoomResponseDTO addRoom(RoomRequestDTO roomRequestDTO) throws RoomAlreadyExistsException;
    RoomResponseDTO getRoomById(Long id) throws ResourceNotFoundException;
    List<RoomResponseDTO> getAllAvailableRooms(int pageNo, int pageSize) throws ResourceNotFoundException;
    RoomResponseDTO updateRoom(Long id, RoomRequestDTO roomRequestDTO) throws ResourceNotFoundException;
    void deleteRoom(Long id) throws ResourceNotFoundException;
    List<RoomResponseDTO> getAllRooms(int pageNo, int pageSize) throws ResourceNotFoundException;
}
