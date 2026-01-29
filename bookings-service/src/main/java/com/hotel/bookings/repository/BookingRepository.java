package com.hotel.bookings.repository;

import com.hotel.bookings.model.Booking;
import com.hotel.bookings.model.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByGuestId(Long guestId, Pageable pageable);
    List<Booking> findByRoomIdAndStatus(Long roomId, BookingStatus status);

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.roomId = :roomId " +
            "AND b.status != 'CANCELLED' " +
            "AND (b.checkInDate < :checkOut AND b.checkOutDate > :checkIn)")
    boolean existsOverlappingBooking(@Param("roomId") Long roomId,
                                     @Param("checkIn") LocalDateTime checkIn,
                                     @Param("checkOut") LocalDateTime checkOut);

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.roomId = :roomId " +
            "AND b.status != 'CANCELLED' " +
            "AND b.id != :bookingId " +
            "AND (b.checkInDate < :checkOut AND b.checkOutDate > :checkIn)")
    boolean existsOverlappingBooking(@Param("roomId") Long roomId,
                                     @Param("checkIn") LocalDateTime checkIn,
                                     @Param("checkOut") LocalDateTime checkOut,
                                     @Param("bookingId") Long bookingId);
}
