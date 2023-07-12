package com.bitsva.RepairAgency.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/home")
    public String homePage() {
        return "main/home";
    }

    @GetMapping("/aboutUs")
    public String aboutUsPage() {
        return "main/about-us";
    }

    @GetMapping("/contacts")
    public String contactsPage() {
        return "main/contacts";
    }
}
