package com.hotel.bookings.service;

import com.hotel.common.exception.BookingConflictException;
import com.hotel.common.exception.ResourceNotFoundException;
import com.hotel.bookings.dto.BookingRequestDTO;
import com.hotel.bookings.dto.BookingResponseDTO;

import java.util.List;

public interface BookingService {
    BookingResponseDTO createBooking(BookingRequestDTO request) throws ResourceNotFoundException, BookingConflictException;
    void cancelBooking(Long bookingId) throws ResourceNotFoundException;
    void checkIn(Long bookingId) throws ResourceNotFoundException;
    void checkOut(Long bookingId) throws ResourceNotFoundException;
    BookingResponseDTO getBookingById(Long id) throws ResourceNotFoundException;
    List<BookingResponseDTO> getBookingsByGuest(Long guestId, int pageNo, int pageSize) throws ResourceNotFoundException;
    List<BookingResponseDTO> getAllBookings(int pageNo, int pageSize) throws ResourceNotFoundException;
    BookingResponseDTO updateBooking(Long id, BookingRequestDTO request) throws ResourceNotFoundException, BookingConflictException;
    void deleteBooking(Long id) throws ResourceNotFoundException;
}
