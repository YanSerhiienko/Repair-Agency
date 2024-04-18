package com.bitsva.RepairAgency.service;

import com.bitsva.RepairAgency.entity.Feedback;
import com.bitsva.RepairAgency.entity.RepairRequest;
import com.bitsva.RepairAgency.entity.user.Repairer;
import com.bitsva.RepairAgency.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserService userService;
    private final RepairRequestService requestService;

    public void save(Feedback feedback) {
        feedbackRepository.save(feedback);

        float averageRating = feedbackRepository.averageRepairerRating(feedback.getRepairerId());
        System.out.println("feedback.toString() = " + feedback.toString());
        //User repairer = userService.getById(feedback.getRepairerId());
        Repairer repairer = userService.getRepairerById(feedback.getRepairerId());
        repairer.setRating(averageRating);
        userService.save(repairer);
        //TODO clean this up
        /*RepairRequest request = requestService.getById(feedback.getRequestId());
        request.setFeedback(feedback);
        requestService.save(request);*/

        RepairRequest request = requestService.getById(feedback.getRequestId());
        request.setHasFeedback(true);
        requestService.save(request);
    }

    public Feedback getById(long requestId) {
        return feedbackRepository.findByRequestId(requestId);
    }
}
