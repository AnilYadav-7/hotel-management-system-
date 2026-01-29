package com.hotel.bookings.controller;

import com.hotel.common.exception.BookingConflictException;
import com.hotel.common.exception.ResourceNotFoundException;
import com.hotel.bookings.dto.BookingRequestDTO;
import com.hotel.bookings.dto.BookingResponseDTO;
import com.hotel.bookings.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'RECEPTIONIST', 'USER')")
    public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestBody BookingRequestDTO request) 
            throws BookingConflictException, ResourceNotFoundException {
        BookingResponseDTO newBooking = bookingService.createBooking(request);
        return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'RECEPTIONIST', 'USER')")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) 
            throws ResourceNotFoundException {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/guest/{guestId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'RECEPTIONIST', 'USER')")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByGuest(
            @PathVariable Long guestId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) throws ResourceNotFoundException {
        return ResponseEntity.ok(bookingService.getBookingsByGuest(guestId, pageNo, pageSize));
    }

    @PatchMapping("/{id}/check-in")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<String> checkIn(@PathVariable Long id) throws ResourceNotFoundException {
        bookingService.checkIn(id);
        return ResponseEntity.ok("Booking checked-in successfully.");
    }

    @PatchMapping("/{id}/check-out")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<String> checkOut(@PathVariable Long id) throws ResourceNotFoundException {
        bookingService.checkOut(id);
        return ResponseEntity.ok("Booking checked-out successfully.");
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER')")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) throws ResourceNotFoundException {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled successfully.");
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'RECEPTIONIST')")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) throws ResourceNotFoundException {
        return ResponseEntity.ok(bookingService.getAllBookings(pageNo, pageSize));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<BookingResponseDTO> updateBooking(@PathVariable Long id,
                                                           @Valid @RequestBody BookingRequestDTO request) 
            throws ResourceNotFoundException, BookingConflictException {
        return ResponseEntity.ok(bookingService.updateBooking(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) 
            throws ResourceNotFoundException {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok("Booking deleted successfully");
    }
}
