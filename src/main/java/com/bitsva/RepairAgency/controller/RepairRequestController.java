package com.bitsva.RepairAgency.controller;

import com.bitsva.RepairAgency.entity.RepairRequest;
import com.bitsva.RepairAgency.feature.RepairRequestCompletionStatus;
import com.bitsva.RepairAgency.feature.RepairRequestPaymentStatus;
import com.bitsva.RepairAgency.service.RepairRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("RepairAgency/")
public class RepairRequestController {
    private final RepairRequestService requestService;

    @GetMapping("/requests")
    public String requestsList(Model model) {
        return findPaginated(1, model);
    }

    @GetMapping("/requests/page/{pageNumber}")
    public String findPaginated(@PathVariable ("pageNumber") int pageNumber, Model model) {
        int pageSize = 5;
        Page<RepairRequest> page = requestService.findPaginated(pageNumber, pageSize);
        List<RepairRequest> requestList = page.getContent();

        System.out.println("page.getNumber() = " + page.getNumber());
        System.out.println("pageNumber = " + pageNumber);

        model.addAttribute("page", page);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("requests", requestList);
        return "request/request-list";
    }


    @PostMapping("/saveRequest")
    public String saveRequest(@ModelAttribute("request") RepairRequest request) {
        requestService.save(request);
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

    //TODO combine editRequest and requestInfo after adding users
    @GetMapping("/requestInfo")
    public String requestInfo(@RequestParam(value = "id") long id, Model model) {
        RepairRequest request = requestService.getById(id);
        model.addAttribute("request", request);
        return "request/request-info";
    }

    @PostMapping("/deleteRequest")
    public String deleteRequest(@RequestParam(value = "id") long id) {
        requestService.deleteById(id);
        return "redirect:/RepairAgency/requests";
    }

    @PostMapping("changePaymentStatus")
    public String changePaymentStatus(@RequestParam(value = "id") long id,
                                      @RequestParam(value = "paymentStatus") RepairRequestPaymentStatus paymentStatus) {
        requestService.changePaymentStatus(id, paymentStatus);
        return "redirect:/RepairAgency/requests";
    }

    @PostMapping("changeCompletionStatus")
    public String changeCompletionStatus(@RequestParam(value = "id") long id,
                                      @RequestParam(value = "completionStatus") RepairRequestCompletionStatus completionStatus) {
        requestService.changeCompletionStatus(id, completionStatus);
        return "redirect:/RepairAgency/requests";
    }

    /*@PostMapping("/saveRequest")
    public String saveRequest(@RequestParam String description) {
        RepairRequest request = new RepairRequest();
        request.setDescription(description);
        requestService.save(request);
        return "redirect:/RepairAgency/requests";
    }*/

    /*@GetMapping("/createRequest")
    public String createRequest() {
        return "create-request";
    }*/

    /*@PostMapping("/editRequest")
    public String editRequest(@RequestParam(value = "id") long id, Model model) {
        RepairRequest request = requestService.getById(id);
        model.addAttribute("request", request);
        return "edit-request";
    }*/

    /*@GetMapping("/searchByQuery")
    public String searchByQuery(@RequestParam(value = "completionStatus", required = false) String completionStatus,
                                @RequestParam(value = "paymentStatus", required = false) String paymentStatus,
                                Model model) {
        model.addAttribute("requests", requestService.searchByQuery(completionStatus, paymentStatus));
        return "request/request-list";
    }*/
}

