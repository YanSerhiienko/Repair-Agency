package com.bitsva.RepairAgency.controller;

import com.bitsva.RepairAgency.config.CustomUserDetails;
import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "user/account/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        System.out.println("user.toString() = " + user.toString());
        return "/user/account/register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               Model model) {

        if (userService.checkIfEmailExists(user.getEmail())) {
            FieldError emailError = new FieldError("user", "email", "User with such email already exists");
            result.addError(emailError);
        }
        if (userService.checkIfPhoneExists(user.getPhone())) {
            FieldError phoneError = new FieldError("user", "phone", "User with such phone already exists");
            result.addError(phoneError);
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "/user/account/register";
        }

        userService.save(user);
        return "redirect:/register?success";
    }

    @GetMapping("/profile")
    public String viewAccount(Principal loggedUser, Model model) {
        User user = userService.findUserByEmail(loggedUser.getName());
        model.addAttribute("user", user);
        return "/user/account/profile";
    }

    @PostMapping("/profileUpdate")
    public String updateAccount(@AuthenticationPrincipal CustomUserDetails loggedUser,
                                @RequestParam(name = "password", required = false) String password,
                                @Valid @ModelAttribute("user") User user,
                                BindingResult result, Model model) {

        if (userService.checkEmailForExistingUser(user.getEmail(), user.getId())) {
            FieldError emailError = new FieldError("user", "email", "User with such email already exists");
            result.addError(emailError);
        }
        if (userService.checkPhoneForExistingUser(user.getPhone(), user.getId())) {
            FieldError phoneError = new FieldError("user", "phone", "User with such phone already exists");
            result.addError(phoneError);
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "/user/account/profile";
        }

        userService.update(user, password);
        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setLastName(user.getLastName());

        return "redirect:/profile?success";
    }

    @GetMapping("/balance")
    public String viewBalance() {
        return "user/account/balance-page";
    }

    @PostMapping("/updateBalance")
    public String updateBalance(@AuthenticationPrincipal CustomUserDetails loggedUser,
                                @RequestParam(value = "amountOfMoney") long amountOfMoney) {
        userService.updateBalance(loggedUser, amountOfMoney);
        return "redirect:/balance?success";
    }
}
