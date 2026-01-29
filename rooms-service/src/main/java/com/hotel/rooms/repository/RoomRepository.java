package com.hotel.rooms.repository;

import com.hotel.rooms.model.Room;
import com.hotel.rooms.model.RoomStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByRoomNumber(String roomNumber);
    Page<Room> findByStatus(RoomStatus status, Pageable pageable);
    Optional<Room> findByRoomNumber(String roomNumber);
}
