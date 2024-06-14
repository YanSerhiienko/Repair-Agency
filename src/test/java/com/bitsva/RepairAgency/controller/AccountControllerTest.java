package com.bitsva.RepairAgency.controller;

import com.bitsva.RepairAgency.config.CustomUserDetails;
import com.bitsva.RepairAgency.config.WithMockCustomUser;
import com.bitsva.RepairAgency.dto.UserCreationDTO;
import com.bitsva.RepairAgency.dto.UserUpdateDTO;
import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.feature.UserRole;
import com.bitsva.RepairAgency.service.impl.UserServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class AccountControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    @MockBean
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @SneakyThrows
    public void login() {
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/account/login"));
    }

    @Test
    @SneakyThrows
    public void showRegistrationForm() {
        mvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/account/register"))
                .andExpect(model().attribute("user", notNullValue()));
    }

    @Test
    @SneakyThrows
    public void registration() {
        UserCreationDTO dto = getCreationDTO();

        when(userServiceImpl.save(dto)).thenReturn(true);

        mvc.perform(post("/register/save").flashAttr("user", dto))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/register?success"));

        verify(userServiceImpl, times(1)).save(dto);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    public void viewAccount() {
        when(userServiceImpl.findUserByEmail("testuser@mail.com")).thenReturn(getUser());

        mvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/account/profile"));

        verify(userServiceImpl, times(1)).findUserByEmail("testuser@mail.com");
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    public void updateAccount() {
        UserUpdateDTO dto = getUpdateDTO();

        when(userServiceImpl.update(dto)).thenReturn(true);

        mvc.perform(post("/profileUpdate").flashAttr("user", dto))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/profile?success"));

        verify(userServiceImpl, times(1)).update(dto);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    public void viewBalance() {
        mvc.perform(get("/balance"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/account/balance-page"));
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    public void updateBalance() {
        CustomUserDetails userDetails = getUserDetails();
        long amountOfMoney = 100L;

        mvc.perform(post("/updateBalance")
                        .param("amountOfMoney", String.valueOf(amountOfMoney))
                        .with(user(userDetails))
                )
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/balance?success"));

        verify(userServiceImpl, times(1)).updateBalance(userDetails, amountOfMoney);

    }

    private CustomUserDetails getUserDetails() {
        User user = User.builder()
                .id(1L)
                .role(UserRole.ROLE_CLIENT)
                .email("testuser@mail.com")
                .firstName("Name")
                .lastName("Surname")
                .build();

        return new CustomUserDetails(user);
    }

    private User getUser() {
        return User.builder()
                .id(1L)
                .role(UserRole.ROLE_CLIENT)
                .email("testuser@mail.com")
                .firstName("Test")
                .lastName("User")
                .password("password")
                .build();
    }

    private UserUpdateDTO getUpdateDTO() {
        return UserUpdateDTO.builder()
                .id(1L)
                .role(UserRole.ROLE_CLIENT)
                .email("testuser@mail.com")
                .firstName("Test")
                .lastName("User")
                .build();
    }

    private UserCreationDTO getCreationDTO() {
        return UserCreationDTO.builder()
                .role(UserRole.ROLE_CLIENT)
                .email("testuser@mail.com")
                .firstName("Test")
                .lastName("User")
                .phone("0991234567")
                .password("password")
                .build();
    }
}
