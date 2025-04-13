package com.application.vaccine_system.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.vaccine_system.exception.InvalidException;
import com.application.vaccine_system.model.Room;
import com.application.vaccine_system.model.response.Pagination;
import com.application.vaccine_system.repository.BookingRepository;
import com.application.vaccine_system.repository.RoomRepository;

import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.stream.Collectors;
import com.application.vaccine_system.dto.response.AvailableRoomResponse;
import com.application.vaccine_system.model.Room.RoomType;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    @Transactional(readOnly = true)
    public Room getRoomById(Long id) throws InvalidException {
        return roomRepository.findById(id)
                .orElseThrow(() -> new InvalidException("Room not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Pagination getAllRooms(Specification<Room> specification, Pageable pageable) {
        Page<Room> pageRoom = roomRepository.findAll(specification, pageable);
        Pagination pagination = new Pagination();
        Pagination.Meta meta = new Pagination.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageRoom.getTotalPages());
        meta.setTotal(pageRoom.getTotalElements());

        pagination.setMeta(meta);

        List<Room> listRooms = new ArrayList<>(pageRoom.getContent());

        pagination.setResult(listRooms);
        return pagination;
    }

    @Transactional
    public Room createRoom(Room room) throws InvalidException {
        if (roomRepository.existsByRoomNumber(room.getRoomNumber())) {
            throw new InvalidException("Room already exists with name: " + room.getRoomNumber());
        }
        room.setAvailable(true);
        return roomRepository.save(room);
    }

    @Transactional
    public Room updateRoom(Long id, Room room) throws InvalidException {
        if (!roomRepository.existsById(id)) {
            throw new InvalidException("Room not found with id: " + id);
        }
        room.setId(id);
        return roomRepository.save(room);
    }

    @Transactional
    public void deleteRoom(Long id) throws InvalidException {
        if (!roomRepository.existsById(id)) {
            throw new InvalidException("Room not found with id: " + id);
        }
        Room room = roomRepository.findById(id).get();
        room.setDeleted(true);
        roomRepository.save(room);
    }

    @Transactional(readOnly = true)
    public List<AvailableRoomResponse> checkAvailableRooms(
            LocalDate checkInDate, 
            LocalDate checkOutDate, 
            RoomType roomType, 
            int numberOfGuests) throws InvalidException {
        
        if (checkInDate.isBefore(LocalDate.now())) {
            throw new InvalidException("Check-in date cannot be in the past");
        }
        if (checkOutDate.isBefore(checkInDate)) {
            throw new InvalidException("Check-out date must be after check-in date");
        }

        // Find all rooms of the specified type that can accommodate the number of guests
        List<Room> rooms = roomRepository.findByTypeAndMaxOccupancyGreaterThanEqualAndIsAvailableTrue(
            roomType, numberOfGuests);

        // Filter out rooms that are already booked for the given dates
        return rooms.stream()
            .filter(room -> !bookingRepository.existsByRoomIdAndDateRange(
                room.getId(), checkInDate, checkOutDate))
            .map(AvailableRoomResponse::fromEntity)
            .collect(Collectors.toList());
    }
}
