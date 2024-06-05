package com.bitsva.RepairAgency.controller;

import com.bitsva.RepairAgency.entity.Feedback;
import com.bitsva.RepairAgency.entity.RepairRequest;
import com.bitsva.RepairAgency.service.FeedbackService;
import com.bitsva.RepairAgency.service.RepairRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;
    private final RepairRequestService requestService;

    @GetMapping("/addFeedback")
    public String addFeedback(@RequestParam(value = "id") long id, Model model) {
        RepairRequest request = requestService.getById(id);
        Feedback feedback = new Feedback();
        feedback.setRequestId(request.getId());
        feedback.setClientId(request.getClientId());
        feedback.setRepairerId(request.getRepairerId());
        model.addAttribute("feedback", feedback);
        return "feedback/feedback-form";
    }

    @PostMapping("/saveFeedback")
    public String saveFeedback(@Valid @ModelAttribute("feedback") Feedback feedback,
                               BindingResult result, Model model) {
        if(result.hasErrors()){
            model.addAttribute("feedback", feedback);
            return "feedback/feedback-form";
        }
        feedbackService.save(feedback);
        return "redirect:/RepairAgency/requests";
    }

    @GetMapping("{id}/feedback")
    public String feedbackInfo(@PathVariable(name = "id") long id, Model model) {
        Feedback feedback = feedbackService.getById(id);
        model.addAttribute("feedback", feedback);
        return "feedback/feedback-info";
    }
}
