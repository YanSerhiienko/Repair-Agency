package com.bitsva.RepairAgency.service;

import com.bitsva.RepairAgency.entity.Feedback;
import com.bitsva.RepairAgency.entity.RepairRequest;
import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserService userService;
    private final RepairRequestService requestService;

    @Transactional
    public void save(Feedback feedback) {
        feedbackRepository.save(feedback);
        float averageRating = feedbackRepository.averageRepairerRating(feedback.getRepairerId());

        User repairer = userService.getById(feedback.getRepairerId());
        repairer.setRating(averageRating);
        userService.save(repairer);

        RepairRequest request = requestService.getById(feedback.getRequestId());
        request.setHasFeedback(true);
        requestService.save(request);
    }

    public Feedback getById(long requestId) {
        return feedbackRepository.findByRequestId(requestId);
    }
}
