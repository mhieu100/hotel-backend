package com.application.vaccine_system.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.vaccine_system.dto.BookingDTO;
import com.application.vaccine_system.model.Booking;
import com.application.vaccine_system.model.Room;
import com.application.vaccine_system.model.User;
import com.application.vaccine_system.model.Booking.BookingStatus;
import com.application.vaccine_system.model.response.Pagination;
import com.application.vaccine_system.repository.BookingRepository;
import com.application.vaccine_system.repository.RoomRepository;
import com.application.vaccine_system.exception.InvalidBookingException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    public Pagination<BookingDTO> getAllBookings(Specification<Booking> specification, Pageable pageable) {
        Page<Booking> pageBooking = bookingRepository.findAll(specification, pageable);
        List<BookingDTO> dtos = pageBooking.getContent().stream()
                .map(BookingDTO::fromEntity)
                .collect(Collectors.toList());

        Pagination<BookingDTO> pagination = new Pagination<>();
        Pagination.Meta meta = new Pagination.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageBooking.getTotalPages());
        meta.setTotal(pageBooking.getTotalElements());

        pagination.setMeta(meta);
        pagination.setResult(dtos);

        return pagination;
    }

    public BookingDTO createBooking(User user, Long roomId, LocalDate checkInDate, 
            LocalDate checkOutDate, String phoneNumber, String specialRequests) {
        if (checkInDate.isBefore(LocalDate.now())) {
            throw new InvalidBookingException("Check-in date cannot be in the past");
        }
        if (checkOutDate.isBefore(checkInDate)) {
            throw new InvalidBookingException("Check-out date must be after check-in date");
        }

        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new InvalidBookingException("Room not found"));

        if (!room.isAvailable()) {
            throw new InvalidBookingException("Room is not available for booking");
        }

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

        return BookingDTO.fromEntity(bookingRepository.save(booking));
    }

    public List<BookingDTO> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(BookingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public BookingDTO getBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new InvalidBookingException("Booking not found"));
        return BookingDTO.fromEntity(booking);
    }

    public BookingDTO updateBookingStatus(Long bookingId, BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new InvalidBookingException("Booking not found"));
        booking.setStatus(status);
        return BookingDTO.fromEntity(bookingRepository.save(booking));
    }

    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new InvalidBookingException("Booking not found"));
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