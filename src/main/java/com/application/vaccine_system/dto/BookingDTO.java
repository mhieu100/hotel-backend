package com.application.vaccine_system.dto;

import java.time.LocalDate;

import com.application.vaccine_system.model.Booking;
import com.application.vaccine_system.model.Booking.BookingStatus;

import lombok.Data;

@Data
public class BookingDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long roomId;
    private String roomNumber;
    private String phoneNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String specialRequests;
    private BookingStatus status;

    public static BookingDTO fromEntity(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setUserId(booking.getUser().getId());
        dto.setUserName(booking.getUser().getFullname());
        dto.setRoomId(booking.getRoom().getId());
        dto.setRoomNumber(booking.getRoom().getRoomNumber());
        dto.setPhoneNumber(booking.getPhoneNumber());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setSpecialRequests(booking.getSpecialRequests());
        dto.setStatus(booking.getStatus());
        return dto;
    }
} 