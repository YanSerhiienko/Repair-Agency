package com.bitsva.RepairAgency.controller;

import com.bitsva.RepairAgency.config.CustomUserDetails;
import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.feature.RepairRequestCompletionStatus;
import com.bitsva.RepairAgency.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;

    /*@GetMapping("/index")
    public String home(){
        return "guide-register-login/index";
    }*/

    @GetMapping("/login")
    public String login(){
        return "user/account/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        // create model object to store form data
        User user = new User();
        model.addAttribute("user", user);
        return "/user/account/register";
    }

    @PostMapping("/register/save")
    public String registration(@Validated @ModelAttribute("user") User user,
                               BindingResult result,
                               Model model){

        if(result.hasErrors()){
            model.addAttribute("user", user);
            return "/user/account/register";
        }

        /*if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }*/



        userService.save(user);
        return "redirect:/register?success";
    }

    @GetMapping("/profile")
    public String viewAccount(Principal loggedUser, Model model) {
        //TODO clean this method
        System.out.println("loggedUser.getName() = " + loggedUser.getName());
        String name = loggedUser.getName();
        User user1 = userService.findUserByEmail(name);
        System.out.println("user1 = " + user1);
        User user = userService.findUserByEmail(loggedUser.getName());
        model.addAttribute("user", user);
        return "/user/account/profile";
    }

    @PostMapping("/profileUpdate")
    public String updateAccount(@AuthenticationPrincipal CustomUserDetails loggedUser,
                                @RequestParam(name = "password", required = false) String password, User user) {

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!userUPDATE = " + user);

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!password = " + password);

        userService.update(user, password);

        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setLastName(user.getLastName());

        //redirectAttributes.addFlashAttribute("message", "Account details have benn updated");
        return "redirect:/RepairAgency/requests";
    }

    @GetMapping("/balance")
    public String viewBalance() {
        return "user/account/balance-page";
    }

    @PostMapping("/updateBalance")
    public String updateBalance(@AuthenticationPrincipal CustomUserDetails loggedUser,
                                @RequestParam(value = "amountOfMoney") long amountOfMoney) {
        userService.updateBalance(loggedUser, amountOfMoney);
        return "/user/account/balance-page";
    }

    public static void main(String[] args) {
        String pass = "111";
        System.out.println("new BCryptPasswordEncoder().encode(111) = " + new BCryptPasswordEncoder().encode(pass));


        String encoded = "$2a$10$kjLq4QbrhA76.XVLUYsL5.4HXqSSCCaUPiiAAkaWGmZk.ze6cUgg2";
    }
}
