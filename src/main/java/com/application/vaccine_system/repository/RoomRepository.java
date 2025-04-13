package com.application.vaccine_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.application.vaccine_system.model.Room;
import com.application.vaccine_system.model.Room.RoomType;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    boolean existsByRoomNumber(String roomNumber);
    List<Room> findByTypeAndMaxOccupancyGreaterThanEqualAndIsAvailableTrue(RoomType type, int maxOccupancy);
}
