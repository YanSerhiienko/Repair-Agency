package com.bitsva.RepairAgency.service.impl;

import com.bitsva.RepairAgency.entity.Feedback;
import com.bitsva.RepairAgency.entity.RepairRequest;
import com.bitsva.RepairAgency.repository.FeedbackRepository;
import com.bitsva.RepairAgency.repository.RepairRequestRepository;
import com.bitsva.RepairAgency.service.FeedbackService;
import com.bitsva.RepairAgency.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class FeedbackServiceImplTest {
    @Autowired
    FeedbackService feedbackService;
    @MockBean
    FeedbackRepository feedbackRepository;
    @MockBean
    RepairRequestRepository requestRepository;
    @MockBean
    UserService userService;

    @Test
    public void save() {
        Feedback feedback = getFeedback();
        RepairRequest request = getRequest();

        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);
        when(feedbackRepository.averageRepairerRating(feedback.getRepairerId())).thenReturn(5);
        doNothing().when(userService).updateRating(5, feedback.getRepairerId());
        when(requestRepository.findById(any())).thenReturn(Optional.ofNullable(request));
        when(requestRepository.save(any())).thenReturn(request);

        feedbackService.save(feedback);

        assertTrue(request.isHasFeedback());
        verify(userService, times(1)).updateRating(5, feedback.getRepairerId());
    }

    @Test
    public void getById() {
        long id = 1L;
        Feedback feedback = getFeedback();

        when(feedbackRepository.findByRequestId(id)).thenReturn(feedback);

        Feedback feedbackById = feedbackService.getById(id);

        assertEquals(feedback, feedbackById);
    }

    private Feedback getFeedback() {
        return Feedback.builder()
                .repairerId(1L)
                .requestId(1L)
                .build();
    }

    private RepairRequest getRequest() {
        return RepairRequest.builder()
                .isHasFeedback(false)
                .build();
    }
}
