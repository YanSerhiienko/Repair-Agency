package com.bitsva.RepairAgency.controller;

import com.bitsva.RepairAgency.config.CustomUserDetails;
import com.bitsva.RepairAgency.entity.RepairRequest;
import com.bitsva.RepairAgency.feature.RepairRequestCompletionStatus;
import com.bitsva.RepairAgency.feature.RepairRequestPaymentStatus;
import com.bitsva.RepairAgency.feature.UserRole;
import com.bitsva.RepairAgency.service.RepairRequestService;
import com.bitsva.RepairAgency.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("RepairAgency/")
public class RepairRequestController {
    private final RepairRequestService requestService;
    private final UserService userService;

    @GetMapping("/requests")
    public String requestsList(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model) {
        return findPaginated(loggedUser, 1, model);
    }

    @GetMapping("/requests/page/{pageNumber}")
    public String findPaginated(@AuthenticationPrincipal CustomUserDetails loggedUser,
                                @PathVariable ("pageNumber") int pageNumber, Model model) {

        int pageSize = 5;
        Page<RepairRequest> page = requestService.findPaginated(pageNumber, pageSize, loggedUser.getRole(), loggedUser.getId());
        List<RepairRequest> requestList = page.getContent();

        model.addAttribute("page", page);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("requests", requestList);
        model.addAttribute("balance", loggedUser.getBalance());

        List<String> repairerList = requestService.repairerList();
        model.addAttribute("repairers", repairerList);

        return "request/request-list";
    }

    @PostMapping("/saveRequest")
    public String saveRequest(@AuthenticationPrincipal CustomUserDetails loggedUser,
                              @Valid @ModelAttribute("request") RepairRequest request,
                              BindingResult result,
                              Model model) {

        if (result.hasErrors()) {
            model.addAttribute("request", request);
            return "request/request-form";
        }
        requestService.save(request, loggedUser);
        return "redirect:/RepairAgency/requests";
    }

    @GetMapping("/createRequest")
    public String createRequest(Model model) {
        RepairRequest request = new RepairRequest();
        model.addAttribute("request", request);
        return "request/request-form";
    }

    @GetMapping("/editRequest")
    public String editRequest(@RequestParam(value = "id") long id, Model model) {
        RepairRequest request = requestService.getById(id);
        model.addAttribute("request", request);
        return "request/request-form";
    }

    @PostMapping("/deleteRequest")
    public String deleteRequest(@RequestParam(value = "id") long id) {
        requestService.deleteById(id);
        return "redirect:/RepairAgency/requests";
    }

    @PostMapping("/changePaymentStatus")
    public String changePaymentStatus(@RequestParam(value = "id") long id,
                                      @RequestParam(value = "paymentStatus") RepairRequestPaymentStatus paymentStatus) {
        requestService.changePaymentStatus(id, paymentStatus);
        return "redirect:/RepairAgency/requests";
    }

    @PostMapping("/changeCompletionStatus")
    public String changeCompletionStatus(@RequestParam(value = "id") long id,
                                      @RequestParam(value = "completionStatus") RepairRequestCompletionStatus completionStatus) {
        requestService.changeCompletionStatus(id, completionStatus);
        return "redirect:/RepairAgency/requests";
    }

    @PostMapping("/updateCost")
    public String updateRequestCost(@RequestParam(value = "id") long id,
                                    @RequestParam(value = "cost") long cost) {
        requestService.updateCost(id, cost);
        List<String> strings = requestService.repairerList();
        return "redirect:/RepairAgency/requests";
    }

    @PostMapping("/updateRepairer")
    public String updateRequestRepairer(@RequestParam(value = "id") long id,
                                    @RequestParam(value = "repairer", required = false) String repairer) {
        requestService.updateRepairer(id, repairer);
        return "redirect:/RepairAgency/requests";
    }

    @PostMapping("/payForRequest")
    public String payForRequest(@RequestParam(value = "id") long id, @AuthenticationPrincipal CustomUserDetails loggedUser) {
        requestService.payForRequest(id, loggedUser);
        return "redirect:/RepairAgency/requests";
    }
}

