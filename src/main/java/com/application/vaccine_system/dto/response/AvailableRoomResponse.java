package com.application.vaccine_system.dto.response;

import java.util.List;

import com.application.vaccine_system.model.Room;
import com.application.vaccine_system.model.Room.RoomFeature;
import com.application.vaccine_system.model.Room.RoomType;

import lombok.Data;

@Data
public class AvailableRoomResponse {
    private Long id;
    private String roomNumber;
    private RoomType type;
    private double pricePerNight;
    private int maxOccupancy;
    private List<RoomFeature> features;
    private String imageUrl;

    public static AvailableRoomResponse fromEntity(Room room) {
        AvailableRoomResponse response = new AvailableRoomResponse();
        response.setId(room.getId());
        response.setRoomNumber(room.getRoomNumber());
        response.setType(room.getType());
        response.setPricePerNight(room.getPricePerNight());
        response.setMaxOccupancy(room.getMaxOccupancy());
        response.setFeatures(room.getFeatures());
        response.setImageUrl(room.getImageUrl());
        return response;
    }
} 