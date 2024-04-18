package com.bitsva.RepairAgency.controller;

import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @GetMapping("/list")
    public String requestsList(Model model) {
        return findPaginated(1, model);
    }

    @GetMapping("/page/{pageNumber}")
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
    public String saveUser(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           Model model) {

        if(result.hasErrors()){
            model.addAttribute("user", user);
            return "user/user-form";
        }

        System.out.println("user.toString() = " + user.toString());
        userService.save(user);
        return "redirect:/users/list";
    }

    @PostMapping("/updateUser")
    public String updateUser(@Valid @ModelAttribute("user") User user,
                             BindingResult result,
                             Model model) {

        if(result.hasErrors()){
            model.addAttribute("user", user);
            return "user/user-form";
        }

        System.out.println("user.toString() = " + user.toString());
        userService.update(user, "");
        return "redirect:/users/list";
    }

    @GetMapping("/createUser")
    public String createUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "user/user-form";
    }

    @GetMapping("/editUser")
    public String editUser(@RequestParam(value = "id") long id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "user/user-form";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam(value = "id") long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }

    @GetMapping("/userInfo/{id}")
    public String userInfo(@PathVariable(value = "id") long id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "user/user-info";
    }

    @PostMapping("/changeAccountStatus")
    public String changeAccountStatus(@RequestParam(value = "id") long id,
                                      @RequestParam(value = "isEnabled", required = false) boolean isEnabled) {
        userService.changeAccountStatus(id, isEnabled);
        System.out.println("isEnabled = " + isEnabled);
        return "redirect:/users/list";
    }
}
