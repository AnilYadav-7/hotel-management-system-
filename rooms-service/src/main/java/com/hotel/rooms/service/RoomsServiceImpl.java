package com.hotel.rooms.service;

import com.hotel.common.exception.ResourceNotFoundException;
import com.hotel.common.exception.RoomAlreadyExistsException;
import com.hotel.rooms.dto.RoomRequestDTO;
import com.hotel.rooms.dto.RoomResponseDTO;
import com.hotel.rooms.model.Room;
import com.hotel.rooms.model.RoomStatus;
import com.hotel.rooms.repository.RoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomsServiceImpl implements RoomsService {

    private final RoomRepository roomRepository;

    public RoomsServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public RoomResponseDTO addRoom(RoomRequestDTO roomRequestDTO) throws RoomAlreadyExistsException {
        if (roomRepository.existsByRoomNumber(roomRequestDTO.getRoomNumber())) {
            throw new RoomAlreadyExistsException("Room " + roomRequestDTO.getRoomNumber() + " already exists!");
        }

        Room room = new Room();
        room.setRoomNumber(roomRequestDTO.getRoomNumber());
        room.setType(roomRequestDTO.getRoomType());
        room.setPrice(roomRequestDTO.getPrice());
        room.setStatus(RoomStatus.AVAILABLE);

        Room savedRoom = roomRepository.save(room);
        return mapToDTO(savedRoom);
    }

    @Override
    public RoomResponseDTO getRoomById(Long id) throws ResourceNotFoundException {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + id));
        return mapToDTO(room);
    }

    @Override
    public List<RoomResponseDTO> getAllAvailableRooms(int pageNo, int pageSize) throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        Page<Room> roomPage = roomRepository.findByStatus(RoomStatus.AVAILABLE, pageable);

        return roomPage.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponseDTO updateRoom(Long id, RoomRequestDTO roomRequestDTO) throws ResourceNotFoundException {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + id));

        room.setType(roomRequestDTO.getRoomType());
        room.setPrice(roomRequestDTO.getPrice());

        return mapToDTO(roomRepository.save(room));
    }

    @Override
    public void deleteRoom(Long id) throws ResourceNotFoundException {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room not found with ID: " + id);
        }
        try {
            roomRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Cannot delete room. It might have active bookings.");
        }
    }

    @Override
    public List<RoomResponseDTO> getAllRooms(int pageNo, int pageSize) throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        Page<Room> roomPage = roomRepository.findAll(pageable);

        return roomPage.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private RoomResponseDTO mapToDTO(Room room) {
        RoomResponseDTO dto = new RoomResponseDTO();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setRoomType(room.getType());
        dto.setPrice(room.getPrice());
        dto.setStatus(room.getStatus());
        return dto;
    }
}
