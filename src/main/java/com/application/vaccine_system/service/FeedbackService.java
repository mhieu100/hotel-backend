package com.application.vaccine_system.service;

import java.time.LocalDate;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.vaccine_system.dto.FeedbackDTO;
import com.application.vaccine_system.model.Feedback;
import com.application.vaccine_system.model.Room;
import com.application.vaccine_system.model.User;
import com.application.vaccine_system.model.response.Pagination;
import com.application.vaccine_system.repository.FeedbackRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackService {
    
    private final FeedbackRepository feedbackRepository;

    public FeedbackDTO createFeedback(User user, int numberStar, LocalDate dateStay, 
            Room.RoomType roomType, String content) {
        
        Feedback feedback = Feedback.builder()
                .user(user)
                .numberStar(numberStar)
                .dateStay(dateStay)
                .roomType(roomType)
                .content(content)
                .createdAt(LocalDate.now())
                .isDeleted(false)
                .build();

        return FeedbackDTO.fromEntity(feedbackRepository.save(feedback));
    }

    public Pagination<FeedbackDTO> getAllFeedbacks(Specification<Feedback> specification, Pageable pageable) {
        Page<Feedback> pageFeedback = feedbackRepository.findAll(specification, pageable);
        
        var dtos = pageFeedback.getContent().stream()
                .map(FeedbackDTO::fromEntity)
                .collect(Collectors.toList());

        Pagination<FeedbackDTO> pagination = new Pagination<>();
        Pagination.Meta meta = new Pagination.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageFeedback.getTotalPages());
        meta.setTotal(pageFeedback.getTotalElements());

        pagination.setMeta(meta);
        pagination.setResult(dtos);

        return pagination;
    }
}