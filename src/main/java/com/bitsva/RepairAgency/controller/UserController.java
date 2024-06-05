package com.bitsva.RepairAgency.controller;

import com.bitsva.RepairAgency.dto.UserCreationDTO;
import com.bitsva.RepairAgency.dto.UserMapper;
import com.bitsva.RepairAgency.dto.UserResponseDTO;
import com.bitsva.RepairAgency.dto.UserUpdateDTO;
import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/list")
    public String requestsList(Model model) {
        return findPaginated(1, model);
    }

    @GetMapping("/page/{pageNumber}")
    public String findPaginated(@PathVariable("pageNumber") int pageNumber, Model model) {
        int pageSize = 5;
        Page<User> page = userService.findPaginated(pageNumber, pageSize);
        List<User> userList = page.getContent();
        List<UserResponseDTO> dtoList = userMapper.mapUserToUserResponseDTO(userList);

        model.addAttribute("page", page);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("users", dtoList);

        return "user/users";
    }

    @PostMapping("/saveUser")
    public String saveUser(@Valid @ModelAttribute("user") UserCreationDTO dto,
                           BindingResult result, Model model) {

        if (userService.checkIfEmailExists(dto.getEmail())) {
            FieldError emailError = new FieldError("user", "email", "User with such email already exists");
            result.addError(emailError);
        }
        if (userService.checkIfPhoneExists(dto.getPhone())) {
            FieldError phoneError = new FieldError("user", "phone", "User with such phone already exists");
            result.addError(phoneError);
        }
        if(result.hasErrors()){
            model.addAttribute("user", dto);
            return "user/user-form";
        }

        userService.save(dto);
        return "redirect:/users/createUser?success";
    }

    @PostMapping("/updateUser")
    public String updateUser(@Valid @ModelAttribute("user") UserUpdateDTO dto,
                             RedirectAttributes redirectAttributes,
                             BindingResult result, Model model) {

        if (userService.checkEmailForExistingUser(dto.getEmail(), dto.getId())) {
            FieldError emailError = new FieldError("user", "email", "User with such email already exists");
            result.addError(emailError);
        }
        if (userService.checkPhoneForExistingUser(dto.getPhone(), dto.getId())) {
            FieldError phoneError = new FieldError("user", "phone", "User with such phone already exists");
            result.addError(phoneError);
        }
        if(result.hasErrors()){
            model.addAttribute("user", dto);
            return "user/user-form";
        }

        userService.update(dto);

        String updateSuccess = "User has been successfully updated!";
        redirectAttributes.addFlashAttribute("updateSuccess", updateSuccess);
        return "redirect:/users/editUser?id=" + dto.getId();
    }

    @GetMapping("/createUser")
    public String createUser(Model model) {
        UserCreationDTO creationDTO = new UserCreationDTO();
        User user = userMapper.mapUserCreationDTOToUser(creationDTO);
        UserUpdateDTO updateDTO = userMapper.mapUserToUserUpdateDTO(user);
        model.addAttribute("user", updateDTO);
        return "user/user-form";
    }

    @GetMapping("/editUser")
    public String editUser(@RequestParam(value = "id") long id, Model model) {
        UserResponseDTO dto = userService.getUserDTO(id);
        model.addAttribute("user", dto);
        return "user/user-form";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam(value = "id") long id,
                             Principal loggedUser) {
        userService.deleteById(id, loggedUser.getName());
        return "redirect:/users/list";
    }

    @GetMapping("/userInfo/{id}")
    public String userInfo(@PathVariable(value = "id") long id, Model model) {
        UserResponseDTO dto = userService.getUserDTO(id);
        model.addAttribute("user", dto);
        return "user/user-info";
    }
}
