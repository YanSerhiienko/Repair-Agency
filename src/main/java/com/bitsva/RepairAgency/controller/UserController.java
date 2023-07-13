package com.bitsva.RepairAgency.controller;

import com.bitsva.RepairAgency.entity.RepairRequest;
import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public String requestsList(Model model) {
        return findPaginated(1, model);
    }

    @GetMapping("/users/page/{pageNumber}")
    public String findPaginated(@PathVariable("pageNumber") int pageNumber, Model model) {
        int pageSize = 5;
        Page<User> page = userService.findPaginated(pageNumber, pageSize);
        List<User> userList = page.getContent();

        System.out.println("page.getNumber() = " + page.getNumber());
        System.out.println("pageNumber = " + pageNumber);

        model.addAttribute("page", page);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("users", userList);
        return "user/users";
    }

    @PostMapping("/saveUser")
    public String saveRequest(@ModelAttribute("user")User user) {
        userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/createUser")
    public String createRequest(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "user/user-form";
    }

    @GetMapping("/editUser")
    public String editRequest(@RequestParam(value = "id") long id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "user/user-form";
    }

    @PostMapping("/deleteUser")
    public String deleteRequest(@RequestParam(value = "id") long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }
}
