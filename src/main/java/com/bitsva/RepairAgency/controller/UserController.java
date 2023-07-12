package com.bitsva.RepairAgency.controller;

import com.bitsva.RepairAgency.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("users")
    public String users() {
        return "user/user-list";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        return "profile";
    }

    @GetMapping("/editProfile")
    public String editProfile() {
        return "profile";
    }
}
