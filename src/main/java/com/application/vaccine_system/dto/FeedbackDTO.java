package com.application.vaccine_system.dto;

import java.time.LocalDate;

import com.application.vaccine_system.model.Feedback;
import com.application.vaccine_system.model.Room.RoomType;

import lombok.Data;

@Data
public class FeedbackDTO {
    private Long id;
    private Long userId;
    private String userName;
    private int numberStar;
    private LocalDate dateStay;
    private RoomType roomType;
    private String content;
    private LocalDate createdAt;

    public static FeedbackDTO fromEntity(Feedback feedback) {
        FeedbackDTO dto = new FeedbackDTO();
        dto.setId(feedback.getId());
        dto.setUserId(feedback.getUser().getId());
        dto.setUserName(feedback.getUser().getFullname());
        dto.setNumberStar(feedback.getNumberStar());
        dto.setDateStay(feedback.getDateStay());
        dto.setRoomType(feedback.getRoomType());
        dto.setContent(feedback.getContent());
        dto.setCreatedAt(feedback.getCreatedAt());
        return dto;
    }
} 