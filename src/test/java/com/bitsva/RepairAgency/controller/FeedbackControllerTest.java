package com.bitsva.RepairAgency.controller;

import com.bitsva.RepairAgency.config.WithMockCustomUser;
import com.bitsva.RepairAgency.entity.Feedback;
import com.bitsva.RepairAgency.entity.RepairRequest;
import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.feature.UserRole;
import com.bitsva.RepairAgency.service.impl.FeedbackServiceImpl;
import com.bitsva.RepairAgency.service.impl.RepairRequestServiceImpl;
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
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class FeedbackControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    @MockBean
    private RepairRequestServiceImpl requestService;
    @MockBean
    private FeedbackServiceImpl feedbackServiceImpl;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    public void addFeedback() {
        long id = 1L;

        when(requestService.getById(id)).thenReturn(getRequest());

        mvc.perform(get("/addFeedback").param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(view().name("feedback/feedback-form"))
                .andExpect(model().attribute("feedback", notNullValue()));

        verify(requestService, times(1)).getById(id);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    public void saveFeedback() {
        long id = 1L;
        Feedback feedback = getFeedback();

        doNothing().when(feedbackServiceImpl).save(feedback);

        mvc.perform(post("/saveFeedback").flashAttr("feedback", feedback))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/RepairAgency/requests"));

        verify(feedbackServiceImpl, times(1)).save(feedback);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    public void feedbackInfo() {
        long id = 1L;

        when(feedbackServiceImpl.getById(id)).thenReturn(getFeedback());

        mvc.perform(get("/requests/{id}/feedback", id))
                .andExpect(status().isOk())
                .andExpect(view().name("feedback/feedback-info"))
                .andExpect(model().attribute("feedback", notNullValue()));

        verify(feedbackServiceImpl, times(1)).getById(id);
    }

    private RepairRequest getRequest() {
        User repairer = User.builder()
                .id(1L)
                .role(UserRole.ROLE_REPAIRER)
                .email("testrepairer@mail.com").build();

        User client = User.builder()
                .id(2L)
                .role(UserRole.ROLE_CLIENT)
                .email("testclient@mail.com").build();

        return RepairRequest.builder()
                .client(client)
                .repairer(repairer)
                .id(1L).build();

    }

    private Feedback getFeedback() {
        return Feedback.builder().build();
    }
}
