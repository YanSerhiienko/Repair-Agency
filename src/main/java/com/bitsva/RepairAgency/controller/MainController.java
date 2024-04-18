package com.bitsva.RepairAgency.controller;

import com.bitsva.RepairAgency.config.CustomUserDetails;
import com.bitsva.RepairAgency.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;

    @GetMapping("/index")
    public String index() {
        return "main/index";
    }

    @GetMapping("/home")
    public String homePage(@AuthenticationPrincipal CustomUserDetails loggedUser) {
        System.out.println("loggedUser.getFullName() = " + loggedUser.getFullName());
        System.out.println("loggedUser = " + loggedUser);
        return "main/home";
    }

    @GetMapping("/about")
    public String aboutUsPage() {
        return "main/about";
    }

    @GetMapping("/contacts")
    public String contactsPage() {
        return "main/contacts";
    }

    ////////////////////////////////////////////////////////////////////


}
