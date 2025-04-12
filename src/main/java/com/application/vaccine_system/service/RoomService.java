package com.application.vaccine_system.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.application.vaccine_system.exception.InvalidException;
import com.application.vaccine_system.model.Room;
import com.application.vaccine_system.model.response.Pagination;
import com.application.vaccine_system.repository.RoomRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public Room getRoomById(Long id) throws InvalidException {
        return roomRepository.findById(id)
                .orElseThrow(() -> new InvalidException("Room not found with id: " + id));
    }

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

    public Room createRoom(Room room) throws InvalidException {
        if (roomRepository.existsByRoomNumber(room.getRoomNumber())) {
            throw new InvalidException("Room already exists with name: " + room.getRoomNumber());
        }
        return roomRepository.save(room);
    }

    public Room updateRoom(Long id, Room room) throws InvalidException {
        if (!roomRepository.existsById(id)) {
            throw new InvalidException("Room not found with id: " + id);
        }
        room.setId(id);
        return roomRepository.save(room);

    }

    public void deleteRoom(Long id) throws InvalidException {
        if (!roomRepository.existsById(id)) {
            throw new InvalidException("Room not found with id: " + id);
        }
        Room room = roomRepository.findById(id).get();
        room.setDeleted(true);
        roomRepository.save(room);
    }
}
