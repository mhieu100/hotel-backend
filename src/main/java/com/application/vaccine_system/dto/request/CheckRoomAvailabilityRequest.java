package com.application.vaccine_system.dto.request;

import java.time.LocalDate;

import com.application.vaccine_system.model.Room.RoomType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckRoomAvailabilityRequest {
    @NotNull(message = "Check-in date is required")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date is required")
    private LocalDate checkOutDate;

    @NotNull(message = "Room type is required")
    private RoomType roomType;

    @NotNull(message = "Number of guests is required")
    @Min(value = 1, message = "Minimum 1 guest required")
    @Max(value = 4, message = "Maximum 4 guests allowed")
    private Integer numberOfGuests;
} 