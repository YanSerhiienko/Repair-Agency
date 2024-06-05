package com.bitsva.RepairAgency.controller;

import com.bitsva.RepairAgency.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
    @Mock
    UserService userService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    void login() {
    }

    @Test
    void showRegistrationForm() {
    }

    @Test
    void registration() {
    }

    @Test
    void viewAccount() {
    }

    @Test
    void updateAccount() {
    }

    @Test
    void viewBalance() {
    }

    @Test
    void updateBalance() {
    }
}