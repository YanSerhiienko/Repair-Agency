package com.bitsva.RepairAgency.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/about")
    public String aboutUsPage() {
        return "main/about";
    }

    @GetMapping("/contacts")
    public String contactsPage() {
        return "main/contacts";
    }
}
