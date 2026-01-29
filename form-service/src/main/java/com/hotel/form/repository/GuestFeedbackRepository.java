package com.hotel.form.repository;

import com.hotel.form.entity.GuestFeedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestFeedbackRepository extends JpaRepository<GuestFeedback, Long> {
    
    Page<GuestFeedback> findAll(Pageable pageable);
    
    List<GuestFeedback> findByGuestNameContainingIgnoreCase(String name);
    
    List<GuestFeedback> findByRoomNumber(String roomNumber);
    
    List<GuestFeedback> findByRating(Integer rating);
    
    List<GuestFeedback> findByRatingBetween(Integer minRating, Integer maxRating);
    
    List<GuestFeedback> findByRatingGreaterThanEqual(Integer minRating);
    
    List<GuestFeedback> findByRatingLessThanEqual(Integer maxRating);
    
    @Query("SELECT g FROM GuestFeedback g WHERE " +
           "LOWER(g.guestName) LIKE LOWER(CONCAT('%', :name, '%')) AND " +
           "g.rating BETWEEN :minRating AND :maxRating")
    List<GuestFeedback> findByNameAndRatingRange(@Param("name") String name, 
                                                  @Param("minRating") Integer minRating, 
                                                  @Param("maxRating") Integer maxRating);
    
    @Query("SELECT g FROM GuestFeedback g WHERE " +
           "LOWER(g.guestName) LIKE LOWER(CONCAT('%', :name, '%')) AND " +
           "g.roomNumber = :roomNumber")
    List<GuestFeedback> findByNameAndRoomNumber(@Param("name") String name, 
                                                @Param("roomNumber") String roomNumber);
    
    @Query("SELECT g FROM GuestFeedback g WHERE " +
           "g.roomNumber = :roomNumber AND " +
           "g.rating BETWEEN :minRating AND :maxRating")
    List<GuestFeedback> findByRoomNumberAndRatingRange(@Param("roomNumber") String roomNumber,
                                                       @Param("minRating") Integer minRating,
                                                       @Param("maxRating") Integer maxRating);
    
    @Query("SELECT g FROM GuestFeedback g WHERE " +
           "LOWER(g.guestName) LIKE LOWER(CONCAT('%', :name, '%')) AND " +
           "g.roomNumber = :roomNumber AND " +
           "g.rating BETWEEN :minRating AND :maxRating")
    List<GuestFeedback> findByNameRoomAndRating(@Param("name") String name,
                                                @Param("roomNumber") String roomNumber,
                                                @Param("minRating") Integer minRating,
                                                @Param("maxRating") Integer maxRating);
}
