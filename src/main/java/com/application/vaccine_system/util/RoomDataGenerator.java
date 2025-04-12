package com.application.vaccine_system.util;

import java.util.Arrays;
import java.util.List;

import com.application.vaccine_system.model.Room;

public class RoomDataGenerator {

    public static List<Room> generateSampleRooms() {
        return Arrays.asList(
                new Room("101", Room.RoomType.SINGLE, 89.99, 1,
                        Arrays.asList(Room.RoomFeature.WIFI, Room.RoomFeature.TV), true),

                new Room("102", Room.RoomType.SINGLE, 89.99, 1,
                        Arrays.asList(Room.RoomFeature.WIFI, Room.RoomFeature.TV, Room.RoomFeature.AIR_CONDITIONING),
                        true),

                new Room("103", Room.RoomType.DOUBLE, 129.99, 2,
                        Arrays.asList(Room.RoomFeature.WIFI, Room.RoomFeature.TV, Room.RoomFeature.AIR_CONDITIONING),
                        true),

                new Room("104", Room.RoomType.DOUBLE, 139.99, 2,
                        Arrays.asList(Room.RoomFeature.WIFI, Room.RoomFeature.TV, Room.RoomFeature.AIR_CONDITIONING,
                                Room.RoomFeature.MINIBAR),
                        true),

                new Room("105", Room.RoomType.SUITE, 199.99, 4,
                        Arrays.asList(Room.RoomFeature.WIFI, Room.RoomFeature.TV, Room.RoomFeature.AIR_CONDITIONING,
                                Room.RoomFeature.MINIBAR, Room.RoomFeature.BALCONY),
                        true),

                new Room("106", Room.RoomType.SUITE, 219.99, 4,
                        Arrays.asList(Room.RoomFeature.WIFI, Room.RoomFeature.TV, Room.RoomFeature.AIR_CONDITIONING,
                                Room.RoomFeature.MINIBAR, Room.RoomFeature.SEA_VIEW),
                        true),

                new Room("201", Room.RoomType.SINGLE, 94.99, 1,
                        Arrays.asList(Room.RoomFeature.WIFI, Room.RoomFeature.TV), true),

                new Room("202", Room.RoomType.SINGLE, 94.99, 1,
                        Arrays.asList(Room.RoomFeature.WIFI, Room.RoomFeature.TV, Room.RoomFeature.AIR_CONDITIONING),
                        false),

                new Room("203", Room.RoomType.DOUBLE, 134.99, 2,
                        Arrays.asList(Room.RoomFeature.WIFI, Room.RoomFeature.TV, Room.RoomFeature.AIR_CONDITIONING),
                        true),

                new Room("204", Room.RoomType.DOUBLE, 144.99, 2,
                        Arrays.asList(Room.RoomFeature.WIFI, Room.RoomFeature.TV, Room.RoomFeature.AIR_CONDITIONING,
                                Room.RoomFeature.MINIBAR),
                        true),

                new Room("205", Room.RoomType.SUITE, 209.99, 4,
                        Arrays.asList(Room.RoomFeature.WIFI, Room.RoomFeature.TV, Room.RoomFeature.AIR_CONDITIONING,
                                Room.RoomFeature.MINIBAR, Room.RoomFeature.BALCONY),
                        false),

                new Room("206", Room.RoomType.SUITE, 229.99, 4,
                        Arrays.asList(Room.RoomFeature.WIFI, Room.RoomFeature.TV, Room.RoomFeature.AIR_CONDITIONING,
                                Room.RoomFeature.MINIBAR, Room.RoomFeature.SEA_VIEW),
                        true),

                new Room("301", Room.RoomType.DELUXE, 279.99, 2,
                        Arrays.asList(Room.RoomFeature.WIFI, Room.RoomFeature.TV, Room.RoomFeature.AIR_CONDITIONING,
                                Room.RoomFeature.MINIBAR, Room.RoomFeature.SEA_VIEW, Room.RoomFeature.BALCONY),
                        true),

                new Room("302", Room.RoomType.DELUXE, 289.99, 2,
                        Arrays.asList(Room.RoomFeature.WIFI, Room.RoomFeature.TV, Room.RoomFeature.AIR_CONDITIONING,
                                Room.RoomFeature.MINIBAR, Room.RoomFeature.SEA_VIEW, Room.RoomFeature.BALCONY),
                        true),

                new Room("303", Room.RoomType.PRESIDENTIAL, 499.99, 4,
                        Arrays.asList(Room.RoomFeature.WIFI, Room.RoomFeature.TV, Room.RoomFeature.AIR_CONDITIONING,
                                Room.RoomFeature.MINIBAR, Room.RoomFeature.SEA_VIEW, Room.RoomFeature.BALCONY),
                        true),

                new Room("304", Room.RoomType.PRESIDENTIAL, 549.99, 4,
                        Arrays.asList(Room.RoomFeature.WIFI, Room.RoomFeature.TV, Room.RoomFeature.AIR_CONDITIONING,
                                Room.RoomFeature.MINIBAR, Room.RoomFeature.SEA_VIEW, Room.RoomFeature.BALCONY),
                        false),

                new Room("401", Room.RoomType.DOUBLE, 159.99, 2,
                        Arrays.asList(Room.RoomFeature.WIFI, Room.RoomFeature.TV, Room.RoomFeature.AIR_CONDITIONING,
                                Room.RoomFeature.MINIBAR),
                        true),

                new Room("402", Room.RoomType.SUITE, 239.99, 4,
                        Arrays.asList(Room.RoomFeature.WIFI, Room.RoomFeature.TV, Room.RoomFeature.AIR_CONDITIONING,
                                Room.RoomFeature.MINIBAR, Room.RoomFeature.BALCONY),
                        true),

                new Room("403", Room.RoomType.DELUXE, 299.99, 2,
                        Arrays.asList(Room.RoomFeature.WIFI, Room.RoomFeature.TV, Room.RoomFeature.AIR_CONDITIONING,
                                Room.RoomFeature.MINIBAR, Room.RoomFeature.SEA_VIEW),
                        true),

                new Room("404", Room.RoomType.PRESIDENTIAL, 599.99, 4,
                        Arrays.asList(Room.RoomFeature.WIFI, Room.RoomFeature.TV, Room.RoomFeature.AIR_CONDITIONING,
                                Room.RoomFeature.MINIBAR, Room.RoomFeature.SEA_VIEW, Room.RoomFeature.BALCONY),
                        true));
    }
}
