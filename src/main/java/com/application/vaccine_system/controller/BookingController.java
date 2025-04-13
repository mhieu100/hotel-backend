package com.application.vaccine_system.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.application.vaccine_system.dto.BookingDTO;
import com.application.vaccine_system.exception.InvalidException;
import com.application.vaccine_system.model.Booking;
import com.application.vaccine_system.model.Booking.BookingStatus;
import com.application.vaccine_system.model.response.Pagination;
import com.application.vaccine_system.service.BookingService;
import com.application.vaccine_system.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Pagination<BookingDTO>> getAllBookings(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        
        Specification<Booking> spec = Specification.where(null);
        
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        
        if (fromDate != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("checkInDate"), fromDate));
        }
        
        if (toDate != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("checkOutDate"), toDate));
        }

        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("checkInDate").descending());
        return ResponseEntity.ok(bookingService.getAllBookings(spec, pageRequest));
    }

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(
            @RequestParam Long userId,
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam String phoneNumber,
            @RequestParam(required = false) String specialRequests) throws InvalidException {
        
        var user = userService.getUser(userId);
        var booking = bookingService.createBooking(user, roomId, checkInDate, checkOutDate, phoneNumber, specialRequests);
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDTO>> getUserBookings(@PathVariable Long userId) {
        var bookings = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable Long bookingId) {
        var booking = bookingService.getBooking(bookingId);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{bookingId}/status")
    public ResponseEntity<BookingDTO> updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam BookingStatus status) {
        var updatedBooking = bookingService.updateBookingStatus(bookingId, status);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }
} 