package com.hotel.guests.repository;

import com.hotel.guests.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findByPhone(String phone);
    Optional<Guest> findByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByIdProofNumber(String idProofNumber);
}
