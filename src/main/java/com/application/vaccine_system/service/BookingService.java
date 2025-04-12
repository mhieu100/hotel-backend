package com.application.vaccine_system.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.vaccine_system.model.Booking;
import com.application.vaccine_system.model.Room;
import com.application.vaccine_system.model.User;
import com.application.vaccine_system.model.Booking.BookingStatus;
import com.application.vaccine_system.model.response.Pagination;
import com.application.vaccine_system.repository.BookingRepository;
import com.application.vaccine_system.repository.RoomRepository;
import com.application.vaccine_system.exception.ResourceNotFoundException;
import com.application.vaccine_system.exception.InvalidBookingException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    public Pagination getAllBookings(Specification<Booking> specification, Pageable pageable) {
        Page<Booking> pageBooking = bookingRepository.findAll(specification, pageable);
        Pagination pagination = new Pagination();
        Pagination.Meta meta = new Pagination.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageBooking.getTotalPages());
        meta.setTotal(pageBooking.getTotalElements());

        pagination.setMeta(meta);
        pagination.setResult(pageBooking.getContent());

        return pagination;
    }

    public Booking createBooking(User user, Long roomId, LocalDate checkInDate, 
            LocalDate checkOutDate, String phoneNumber, String specialRequests) {
        
        if (checkInDate.isBefore(LocalDate.now())) {
            throw new InvalidBookingException("Check-in date cannot be in the past");
        }
        if (checkOutDate.isBefore(checkInDate)) {
            throw new InvalidBookingException("Check-out date must be after check-in date");
        }

        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        if (!room.isAvailable()) {
            throw new InvalidBookingException("Room is not available for booking");
        }

        // Check if room is already booked for the given dates
        if (isRoomBooked(roomId, checkInDate, checkOutDate)) {
            throw new InvalidBookingException("Room is already booked for these dates");
        }

        Booking booking = Booking.builder()
                .user(user)
                .room(room)
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .phoneNumber(phoneNumber)
                .specialRequests(specialRequests)
                .status(BookingStatus.PENDING)
                .build();

        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    public Booking updateBookingStatus(Long bookingId, BookingStatus status) {
        Booking booking = getBooking(bookingId);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public void cancelBooking(Long bookingId) {
        Booking booking = getBooking(bookingId);
        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new InvalidBookingException("Cannot cancel a completed booking");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    private boolean isRoomBooked(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        return bookingRepository.existsByRoomIdAndDateRange(roomId, checkIn, checkOut);
    }
} 