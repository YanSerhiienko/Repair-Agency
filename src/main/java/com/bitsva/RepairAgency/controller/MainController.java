package com.bitsva.RepairAgency.controller;

import com.bitsva.RepairAgency.config.CustomUserDetails;
import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.feature.UserRole;
import com.bitsva.RepairAgency.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
