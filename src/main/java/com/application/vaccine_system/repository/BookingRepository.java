package com.application.vaccine_system.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.application.vaccine_system.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {
    List<Booking> findByUserId(Long userId);
    
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.room.id = :roomId " +
           "AND b.status != 'CANCELLED' " +
           "AND ((b.checkInDate BETWEEN :checkIn AND :checkOut) " +
           "OR (b.checkOutDate BETWEEN :checkIn AND :checkOut))")
    boolean existsByRoomIdAndDateRange(Long roomId, LocalDate checkIn, LocalDate checkOut);
} 