package com.application.vaccine_system.model;

import java.util.List;


import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Room {
    public Room( String roomNumber, RoomType type, double pricePerNight, int maxOccupancy,
            List<RoomFeature> features, boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.maxOccupancy = maxOccupancy;
        this.features = features;
        this.isAvailable = isAvailable;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String roomNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType type;
    
    @Column(nullable = false)
    private double pricePerNight;
    
    @Column(nullable = false)
    private int maxOccupancy;
    
    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "room_features", joinColumns = @JoinColumn(name = "room_id"))
    private List<RoomFeature> features;
    
    @Column(nullable = false)
    private boolean isAvailable;
    
    public enum RoomType {
        SINGLE, DOUBLE, SUITE, DELUXE, PRESIDENTIAL
    }
    public enum RoomFeature {
        WIFI, TV, AIR_CONDITIONING, MINIBAR, SEA_VIEW, BALCONY
    }
    boolean isDeleted;
}
