package com.bitsva.RepairAgency.controller;

import com.bitsva.RepairAgency.config.CustomUserDetails;
import com.bitsva.RepairAgency.dto.UserCreationDTO;
import com.bitsva.RepairAgency.dto.UserMapper;
import com.bitsva.RepairAgency.dto.UserUpdateDTO;
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
    private final UserMapper userMapper;

    @GetMapping("/login")
    public String login() {
        return "user/account/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserCreationDTO user = new UserCreationDTO();
        model.addAttribute("user", user);
        return "/user/account/register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserCreationDTO dto,
                               BindingResult result,
                               Model model) {

        if (userService.checkIfEmailExists(dto.getEmail())) {
            FieldError emailError = new FieldError("user", "email", "User with such email already exists");
            result.addError(emailError);
        }
        if (userService.checkIfPhoneExists(dto.getPhone())) {
            FieldError phoneError = new FieldError("user", "phone", "User with such phone already exists");
            result.addError(phoneError);
        }
        if (result.hasErrors()) {
            model.addAttribute("user", dto);
            return "/user/account/register";
        }

        userService.save(dto);
        return "redirect:/register?success";
    }

    @GetMapping("/profile")
    public String viewAccount(Principal loggedUser, Model model) {
        User user = userService.findUserByEmail(loggedUser.getName());
        UserUpdateDTO dto = userMapper.mapUserToUserUpdateDTO(user);
        model.addAttribute("user", dto);
        return "/user/account/profile";
    }

    @PostMapping("/profileUpdate")
    public String updateAccount(@AuthenticationPrincipal CustomUserDetails loggedUser,
                                @Valid @ModelAttribute("user") UserUpdateDTO dto,
                                BindingResult result, Model model) {

        if (userService.checkIfEmailLinkedToAnotherUser(dto.getEmail(), dto.getId())) {
            FieldError emailError = new FieldError("user", "email", "User with such email already exists");
            result.addError(emailError);
        }
        if (userService.checkIfPhoneLinkedToAnotherUser(dto.getPhone(), dto.getId())) {
            FieldError phoneError = new FieldError("user", "phone", "User with such phone already exists");
            result.addError(phoneError);
        }
        if (result.hasErrors()) {
            model.addAttribute("user", dto);
            return "/user/account/profile";
        }

        userService.update(dto);
        loggedUser.setFirstName(dto.getFirstName());
        loggedUser.setLastName(dto.getLastName());

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
