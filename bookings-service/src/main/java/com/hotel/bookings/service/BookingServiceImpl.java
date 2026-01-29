package com.hotel.bookings.service;

import com.hotel.common.exception.BookingConflictException;
import com.hotel.common.exception.ResourceNotFoundException;
import com.hotel.bookings.dto.*;
import com.hotel.bookings.model.Booking;
import com.hotel.bookings.model.BookingStatus;
import com.hotel.bookings.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RestTemplate restTemplate;

    @Value("${guests.service.url:http://localhost:8083}")
    private String guestsServiceUrl;

    @Value("${rooms.service.url:http://localhost:8082}")
    private String roomsServiceUrl;

    public BookingServiceImpl(BookingRepository bookingRepository, RestTemplate restTemplate) {
        this.bookingRepository = bookingRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO request) 
            throws BookingConflictException, ResourceNotFoundException {
        
        // Call guests-service to get guest details
        GuestResponseDTO guest = restTemplate.getForObject(
                guestsServiceUrl + "/api/guests/" + request.getGuestId(),
                GuestResponseDTO.class);

        if (guest == null) {
            throw new ResourceNotFoundException("Guest not found with ID: " + request.getGuestId());
        }

        // Call rooms-service to get room details
        RoomResponseDTO room = restTemplate.getForObject(
                roomsServiceUrl + "/api/rooms/" + request.getRoomId(),
                RoomResponseDTO.class);

        if (room == null) {
            throw new ResourceNotFoundException("Room not found with ID: " + request.getRoomId());
        }

        if ("MAINTENANCE".equals(room.getStatus())) {
            throw new IllegalStateException("Room " + room.getRoomNumber() + " is under maintenance and cannot be booked.");
        }

        if (!request.getCheckOutDate().isAfter(request.getCheckInDate())) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        boolean isOccupied = bookingRepository.existsOverlappingBooking(
                request.getRoomId(), request.getCheckInDate(), request.getCheckOutDate());

        if (isOccupied) {
            throw new BookingConflictException("Room is already booked for these dates!");
        }

        long days = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        if (days == 0) days = 1;
        Double totalAmount = room.getPrice() * days;

        Booking booking = new Booking();
        booking.setGuestId(request.getGuestId());
        booking.setRoomId(request.getRoomId());
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setNumberOfAdults(request.getNumberOfAdults());
        booking.setNumberOfChildren(request.getNumberOfChildren());
        booking.setTotalAmount(totalAmount);
        booking.setStatus(BookingStatus.RESERVED);

        Booking savedBooking = bookingRepository.save(booking);
        return mapToDTO(savedBooking, guest, room);
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) throws ResourceNotFoundException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        if (booking.getStatus() == BookingStatus.CHECKED_OUT) {
            throw new IllegalStateException("Cannot cancel a completed booking.");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public void checkIn(Long bookingId) throws ResourceNotFoundException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        if (booking.getStatus() != BookingStatus.RESERVED) {
            throw new IllegalStateException("Booking is not in RESERVED state.");
        }

        LocalDate today = LocalDate.now();
        LocalDate checkInDate = booking.getCheckInDate().toLocalDate();
        LocalDate checkOutDate = booking.getCheckOutDate().toLocalDate();

        if (today.isBefore(checkInDate)) {
            throw new IllegalStateException("Check-in date is in the future. Cannot check-in yet.");
        }

        if (today.isAfter(checkOutDate) || today.isEqual(checkOutDate)) {
            throw new IllegalStateException("Booking expired. Cannot check-in.");
        }

        booking.setStatus(BookingStatus.CHECKED_IN);
        bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public void checkOut(Long bookingId) throws ResourceNotFoundException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        if (booking.getStatus() != BookingStatus.CHECKED_IN) {
            throw new IllegalStateException("Guest has not checked in yet.");
        }

        booking.setStatus(BookingStatus.CHECKED_OUT);
        bookingRepository.save(booking);
    }

    @Override
    public BookingResponseDTO getBookingById(Long id) throws ResourceNotFoundException {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));
        
        GuestResponseDTO guest = restTemplate.getForObject(
                guestsServiceUrl + "/api/guests/" + booking.getGuestId(),
                GuestResponseDTO.class);
        
        RoomResponseDTO room = restTemplate.getForObject(
                roomsServiceUrl + "/api/rooms/" + booking.getRoomId(),
                RoomResponseDTO.class);
        
        return mapToDTO(booking, guest, room);
    }

    @Override
    public List<BookingResponseDTO> getBookingsByGuest(Long guestId, int pageNo, int pageSize) 
            throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Booking> bookingsPage = bookingRepository.findByGuestId(guestId, pageable);

        return bookingsPage.getContent().stream()
                .map(booking -> {
                    GuestResponseDTO guest = restTemplate.getForObject(
                            guestsServiceUrl + "/api/guests/" + booking.getGuestId(),
                            GuestResponseDTO.class);
                    RoomResponseDTO room = restTemplate.getForObject(
                            roomsServiceUrl + "/api/rooms/" + booking.getRoomId(),
                            RoomResponseDTO.class);
                    return mapToDTO(booking, guest, room);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDTO> getAllBookings(int pageNo, int pageSize) throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Booking> bookingsPage = bookingRepository.findAll(pageable);

        return bookingsPage.getContent().stream()
                .map(booking -> {
                    GuestResponseDTO guest = restTemplate.getForObject(
                            guestsServiceUrl + "/api/guests/" + booking.getGuestId(),
                            GuestResponseDTO.class);
                    RoomResponseDTO room = restTemplate.getForObject(
                            roomsServiceUrl + "/api/rooms/" + booking.getRoomId(),
                            RoomResponseDTO.class);
                    return mapToDTO(booking, guest, room);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingResponseDTO updateBooking(Long id, BookingRequestDTO request) 
            throws ResourceNotFoundException, BookingConflictException {
        
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));

        GuestResponseDTO guest = restTemplate.getForObject(
                guestsServiceUrl + "/api/guests/" + request.getGuestId(),
                GuestResponseDTO.class);

        if (guest == null) {
            throw new ResourceNotFoundException("Guest not found with ID: " + request.getGuestId());
        }

        // Call rooms-service to get room details
        RoomResponseDTO room = restTemplate.getForObject(
                roomsServiceUrl + "/api/rooms/" + request.getRoomId(),
                RoomResponseDTO.class);

        if (room == null) {
            throw new ResourceNotFoundException("Room not found with ID: " + request.getRoomId());
        }

        if ("MAINTENANCE".equals(room.getStatus())) {
            throw new IllegalStateException("Room " + room.getRoomNumber() + " is under maintenance and cannot be booked.");
        }

        if (!request.getCheckOutDate().isAfter(request.getCheckInDate())) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        // Check for conflicts only if dates or room changed
        if (!booking.getRoomId().equals(request.getRoomId()) || 
            !booking.getCheckInDate().equals(request.getCheckInDate()) || 
            !booking.getCheckOutDate().equals(request.getCheckOutDate())) {
            
            boolean isOccupied = bookingRepository.existsOverlappingBooking(
                    request.getRoomId(), request.getCheckInDate(), request.getCheckOutDate(), id);

            if (isOccupied) {
                throw new BookingConflictException("Room is already booked for these dates!");
            }
        }

        long days = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        if (days == 0) days = 1;
        Double totalAmount = room.getPrice() * days;

        booking.setGuestId(request.getGuestId());
        booking.setRoomId(request.getRoomId());
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setNumberOfAdults(request.getNumberOfAdults());
        booking.setNumberOfChildren(request.getNumberOfChildren());
        booking.setTotalAmount(totalAmount);

        Booking savedBooking = bookingRepository.save(booking);
        return mapToDTO(savedBooking, guest, room);
    }

    @Override
    @Transactional
    public void deleteBooking(Long id) throws ResourceNotFoundException {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));

        if (booking.getStatus() == BookingStatus.CHECKED_IN) {
            throw new IllegalStateException("Cannot delete a booking that is currently checked in.");
        }

        bookingRepository.delete(booking);
    }

    private BookingResponseDTO mapToDTO(Booking booking, GuestResponseDTO guest, RoomResponseDTO room) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(booking.getId());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setStatus(booking.getStatus());
        dto.setGuest(guest);
        dto.setRoom(room);
        return dto;
    }
}
