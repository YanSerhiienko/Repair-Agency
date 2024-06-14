package com.bitsva.RepairAgency.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
class MainControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @SneakyThrows
    public void homePage() {
        mvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("main/home"));
    }

    @Test
    @SneakyThrows
    public void aboutUsPage() {
        mvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("main/about"));
    }

    @Test
    @SneakyThrows
    public void contactsPage() {
        mvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(view().name("main/contacts"));
    }
}