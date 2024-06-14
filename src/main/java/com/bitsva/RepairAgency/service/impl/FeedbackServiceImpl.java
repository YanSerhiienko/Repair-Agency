package com.bitsva.RepairAgency.service.impl;

import com.bitsva.RepairAgency.entity.Feedback;
import com.bitsva.RepairAgency.entity.RepairRequest;
import com.bitsva.RepairAgency.repository.FeedbackRepository;
import com.bitsva.RepairAgency.repository.RepairRequestRepository;
import com.bitsva.RepairAgency.service.FeedbackService;
import com.bitsva.RepairAgency.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserService userService;
    private final RepairRequestRepository requestRepository;

    @Transactional
    @Override
    public void save(Feedback feedback) {
        feedbackRepository.save(feedback);
        float averageRating = feedbackRepository.averageRepairerRating(feedback.getRepairerId());

        userService.updateRating(averageRating, feedback.getRepairerId());

        RepairRequest request = requestRepository.findById(feedback.getRequestId()).orElse(null);
        request.setHasFeedback(true);
        requestRepository.save(request);
    }

    @Override
    public Feedback getById(long requestId) {
        return feedbackRepository.findByRequestId(requestId);
    }
}
