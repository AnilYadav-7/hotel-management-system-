package com.hotel.rooms.controller;

import com.hotel.common.exception.ResourceNotFoundException;
import com.hotel.common.exception.RoomAlreadyExistsException;
import com.hotel.rooms.dto.RoomRequestDTO;
import com.hotel.rooms.dto.RoomResponseDTO;
import com.hotel.rooms.service.RoomsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomsService roomService;

    public RoomController(RoomsService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<RoomResponseDTO> createRoom(@Valid @RequestBody RoomRequestDTO request) 
            throws RoomAlreadyExistsException {
        RoomResponseDTO newRoom = roomService.addRoom(request);
        return new ResponseEntity<>(newRoom, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'RECEPTIONIST')")
    public ResponseEntity<List<RoomResponseDTO>> getAllRooms(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) throws ResourceNotFoundException {
        return ResponseEntity.ok(roomService.getAllRooms(pageNo, pageSize));
    }

    @GetMapping("/available")
    @PreAuthorize("hasAnyRole('MANAGER', 'RECEPTIONIST', 'USER')")
    public ResponseEntity<List<RoomResponseDTO>> getAvailableRooms(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) throws ResourceNotFoundException {
        return ResponseEntity.ok(roomService.getAllAvailableRooms(pageNo, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> getRoomById(@PathVariable Long id) 
            throws ResourceNotFoundException {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<RoomResponseDTO> updateRoom(@PathVariable Long id,
                                                      @Valid @RequestBody RoomRequestDTO request) 
            throws ResourceNotFoundException {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) 
            throws ResourceNotFoundException {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted successfully");
    }
}
