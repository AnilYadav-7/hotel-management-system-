package com.hotel.guests.controller;

import com.hotel.common.exception.ResourceNotFoundException;
import com.hotel.guests.dto.GuestRequestDTO;
import com.hotel.guests.dto.GuestResponseDTO;
import com.hotel.guests.service.GuestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'RECEPTIONIST')")
    public ResponseEntity<GuestResponseDTO> createGuest(@Valid @RequestBody GuestRequestDTO request) 
            throws ResourceNotFoundException {
        GuestResponseDTO newGuest = guestService.addGuest(request);
        return new ResponseEntity<>(newGuest, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'RECEPTIONIST')")
    public ResponseEntity<GuestResponseDTO> getGuestById(@PathVariable Long id) 
            throws ResourceNotFoundException {
        return ResponseEntity.ok(guestService.getGuestById(id));
    }

    @GetMapping("/phone/{phone}")
    @PreAuthorize("hasAnyRole('MANAGER', 'RECEPTIONIST')")
    public ResponseEntity<GuestResponseDTO> getGuestByPhone(@PathVariable String phone) 
            throws ResourceNotFoundException {
        return ResponseEntity.ok(guestService.getGuestByPhone(phone));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'RECEPTIONIST')")
    public ResponseEntity<GuestResponseDTO> updateGuest(@PathVariable Long id,
                                                        @Valid @RequestBody GuestRequestDTO request) 
            throws ResourceNotFoundException {
        return ResponseEntity.ok(guestService.updateGuest(id, request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'RECEPTIONIST')")
    public ResponseEntity<List<GuestResponseDTO>> getAllGuests(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) throws ResourceNotFoundException {
        return ResponseEntity.ok(guestService.getAllGuests(pageNo, pageSize));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> deleteGuest(@PathVariable Long id) 
            throws ResourceNotFoundException {
        guestService.deleteGuest(id);
        return ResponseEntity.ok("Guest deleted successfully");
    }
}
