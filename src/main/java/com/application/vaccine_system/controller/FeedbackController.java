package com.application.vaccine_system.controller;

import java.time.LocalDate;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.application.vaccine_system.dto.FeedbackDTO;
import com.application.vaccine_system.exception.InvalidException;
import com.application.vaccine_system.model.Feedback;
import com.application.vaccine_system.model.Room;
import com.application.vaccine_system.model.response.Pagination;
import com.application.vaccine_system.service.FeedbackService;
import com.application.vaccine_system.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<FeedbackDTO> createFeedback(
            @RequestParam Long userId,
            @RequestParam int numberStar,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateStay,
            @RequestParam Room.RoomType roomType,
            @RequestParam(required = false) String content) throws InvalidException {
        
        var user = userService.getUser(userId);
        var feedback = feedbackService.createFeedback(user, numberStar, dateStay, roomType, content);
        return new ResponseEntity<>(feedback, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Pagination<FeedbackDTO>> getAllFeedbacks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Room.RoomType roomType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        
        Specification<Feedback> spec = Specification.where(null);
        
        if (roomType != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("roomType"), roomType));
        }
        
        if (fromDate != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dateStay"), fromDate));
        }
        
        if (toDate != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("dateStay"), toDate));
        }

        // Add condition to exclude deleted feedbacks
        spec = spec.and((root, query, cb) -> cb.equal(root.get("isDeleted"), false));

        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(feedbackService.getAllFeedbacks(spec, pageRequest));
    }
} 