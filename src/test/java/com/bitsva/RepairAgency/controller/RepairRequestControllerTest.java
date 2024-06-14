package com.bitsva.RepairAgency.controller;

import com.bitsva.RepairAgency.config.CustomUserDetails;
import com.bitsva.RepairAgency.config.WithMockCustomUser;
import com.bitsva.RepairAgency.entity.RepairRequest;
import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.feature.RepairRequestCompletionStatus;
import com.bitsva.RepairAgency.feature.RepairRequestPaymentStatus;
import com.bitsva.RepairAgency.feature.UserRole;
import com.bitsva.RepairAgency.service.impl.RepairRequestServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class RepairRequestControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    @MockBean
    private RepairRequestServiceImpl requestService;

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
    public void requestsList() {
        CustomUserDetails userDetails = getUserDetails();
        PageImpl<RepairRequest> page = new PageImpl<>(List.of(getRequest()));

        when(requestService.findPaginated(1, 5, userDetails.getRole(), userDetails.getId()))
                .thenReturn(page);

        mvc.perform(get("/RepairAgency/requests")
                        .with(user(userDetails)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("request/request-list"))
                .andExpect(model().attribute("requests", hasSize(1)))
                .andExpect(model().attribute("requests", hasItem(
                        allOf(
                                hasProperty("id", is(1L))
                        )
                )));
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    public void findPaginated() {
        CustomUserDetails userDetails = getUserDetails();
        PageImpl<RepairRequest> page = new PageImpl<>(List.of(getRequest()));

        when(requestService.findPaginated(3, 5, userDetails.getRole(), userDetails.getId()))
                .thenReturn(page);

        mvc.perform(get("/RepairAgency/requests/page/3")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("request/request-list"))
                .andExpect(model().attribute("page", is(page)))
                .andExpect(model().attribute("currentPage", is(3)))
                .andExpect(model().attribute("totalPages", is(page.getTotalPages())))
                .andExpect(model().attribute("totalItems", is(page.getTotalElements())))
                .andExpect(model().attribute("requests", hasSize(1)));
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    public void saveRequest() {
        CustomUserDetails userDetails = getUserDetails();
        RepairRequest request = getRequest();

        doNothing().when(requestService).save(request, userDetails);

        mvc.perform(post("/RepairAgency/saveRequest")
                        .with(user(userDetails))
                        .flashAttr("request", request))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/RepairAgency/requests"));

        verify(requestService, times(1)).save(request, userDetails);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    public void createRequest() {
        mvc.perform(get("/RepairAgency/createRequest"))
                .andExpect(status().isOk())
                .andExpect(view().name("request/request-form"))
                .andExpect(model().attribute("request", notNullValue()));
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    public void editRequest() {
        long id = 1L;

        when(requestService.getById(id)).thenReturn(getRequest());

        mvc.perform(get("/RepairAgency/editRequest?id=" + id))
                .andExpect(status().isOk())
                .andExpect(view().name("request/request-form"))
                .andExpect(model().attribute("request", notNullValue()));

        verify(requestService, times(1)).getById(id);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    public void deleteRequest() {
        long id = 1L;

        mvc.perform(post("/RepairAgency/deleteRequest?id=" + id))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/RepairAgency/requests"));

        verify(requestService, times(1)).deleteById(id);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser(role = UserRole.ROLE_MANAGER)
    public void changePaymentStatus() {
        long id = 1L;
        RepairRequestPaymentStatus paymentStatus = RepairRequestPaymentStatus.PAID;

        mvc.perform(post("/RepairAgency/changePaymentStatus?id=" + id)
                        .param("id", String.valueOf(id))
                        .param("paymentStatus", String.valueOf(paymentStatus))
                )
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/RepairAgency/requests"));

        verify(requestService, times(1)).changePaymentStatus(id, paymentStatus);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser(role = UserRole.ROLE_REPAIRER)
    public void changeCompletionStatus() {
        long id = 1L;
        RepairRequestCompletionStatus completionStatus = RepairRequestCompletionStatus.COMPLETED;

        mvc.perform(post("/RepairAgency/changeCompletionStatus?id=" + id)
                        .param("id", String.valueOf(id))
                        .param("completionStatus", String.valueOf(completionStatus))
                )
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/RepairAgency/requests"));

        verify(requestService, times(1)).changeCompletionStatus(id, completionStatus);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser(role = UserRole.ROLE_MANAGER)
    public void updateRequestCost() {
        long id = 1L;
        long cost = 0l;

        mvc.perform(post("/RepairAgency/updateCost?id=" + id)
                        .param("id", String.valueOf(id))
                        .param("cost", String.valueOf(cost))
                )
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/RepairAgency/requests"));

        verify(requestService, times(1)).updateCost(id, cost);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser(role = UserRole.ROLE_MANAGER)
    public void updateRequestRepairer() {
        long id = 1L;
        String repairer = "repairer";

        mvc.perform(post("/RepairAgency/updateRepairer?id=" + id)
                        .param("id", String.valueOf(id))
                        .param("repairer", repairer)
                )
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/RepairAgency/requests"));

        verify(requestService, times(1)).updateRepairer(id, repairer);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    public void payForRequest() {
        CustomUserDetails userDetails = getUserDetails();
        long id = 1L;

        mvc.perform(post("/RepairAgency/payForRequest?id=" + id)
                        .with(user(userDetails)))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/RepairAgency/requests"));

        verify(requestService, times(1)).payForRequest(id, userDetails);
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
                .cost(0L)
                .depositedToPay(0L)
                .paymentStatus(RepairRequestPaymentStatus.AWAITING_PAYMENT)
                .completionStatus(RepairRequestCompletionStatus.NOT_STARTED)
                .creationDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .description("fix it please")
                .id(1L).build();

    }
}
