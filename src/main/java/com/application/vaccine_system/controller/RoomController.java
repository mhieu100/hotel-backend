package com.application.vaccine_system.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.application.vaccine_system.annotation.ApiMessage;
import com.application.vaccine_system.dto.request.CheckRoomAvailabilityRequest;
import com.application.vaccine_system.dto.response.AvailableRoomResponse;
import com.application.vaccine_system.exception.InvalidException;
import com.application.vaccine_system.model.Room;
import com.application.vaccine_system.model.response.Pagination;
import com.application.vaccine_system.service.RoomService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/rooms")
@AllArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/{id}")
    @ApiMessage("Get a room by id")
    public ResponseEntity<Room> getVaccineById(@PathVariable Long id) throws InvalidException {
        return ResponseEntity.ok().body(roomService.getRoomById(id));
    }

    @GetMapping
    @ApiMessage("Get all rooms")
    public ResponseEntity<Pagination> getAllVaccines(@Filter Specification<Room> specification,
            Pageable pageable) {
                specification = Specification.where(specification).and((root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("isDeleted"), false));
        return ResponseEntity.ok().body(roomService.getAllRooms(specification, pageable));
    }

    @PostMapping
    @ApiMessage("Create a new room")
    public ResponseEntity<Room> createVaccine(@Valid @RequestBody Room room) throws InvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.createRoom(room));
    }

    @PutMapping("/{id}")
    @ApiMessage("Update a room")
    public ResponseEntity<Room> updateVaccine(@PathVariable Long id,@Valid @RequestBody Room room) throws InvalidException {
        return ResponseEntity.ok().body(roomService.updateRoom(id, room));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete a vaccine")
    public void deleteVaccine(@PathVariable Long id) throws InvalidException {
        roomService.deleteRoom(id);
    }

    @GetMapping("/check-availability")
    public ResponseEntity<List<AvailableRoomResponse>> checkAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam Room.RoomType roomType,
            @RequestParam int numberOfGuests) throws InvalidException {
        
        var availableRooms = roomService.checkAvailableRooms(
            checkInDate, checkOutDate, roomType, numberOfGuests);
        return ResponseEntity.ok(availableRooms);
    }

    // Alternative endpoint using request body
    @PostMapping("/check-availability")
    public ResponseEntity<List<AvailableRoomResponse>> checkAvailabilityPost(
            @Valid @RequestBody CheckRoomAvailabilityRequest request) throws InvalidException {
        
        var availableRooms = roomService.checkAvailableRooms(
            request.getCheckInDate(),
            request.getCheckOutDate(),
            request.getRoomType(),
            request.getNumberOfGuests());
        return ResponseEntity.ok(availableRooms);
    }
}
