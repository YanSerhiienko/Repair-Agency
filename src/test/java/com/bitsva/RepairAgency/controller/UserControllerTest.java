package com.bitsva.RepairAgency.controller;

import com.bitsva.RepairAgency.config.WithMockCustomUser;
import com.bitsva.RepairAgency.dto.UserCreationDTO;
import com.bitsva.RepairAgency.dto.UserResponseDTO;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class UserControllerTest {
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
    @WithMockCustomUser(role = UserRole.ROLE_ADMIN)
    public void usersList() {
        when(userServiceImpl.findPaginated(1, 5))
                .thenReturn(new PageImpl<>(List.of(getUser())));

        mvc.perform(get("/users/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/user-list"))
                .andExpect(model().attribute("users", hasSize(1)))
                .andExpect(model().attribute("users", hasItem(
                        allOf(
                                hasProperty("email", is("testuser@mail.com"))
                        )
                )));
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser(role = UserRole.ROLE_ADMIN)
    public void findPaginated() {
        PageImpl<User> page = new PageImpl<>(List.of(getUser()));
        when(userServiceImpl.findPaginated(3, 5))
                .thenReturn(page);

        mvc.perform(get("/users/list/page/3"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user-list"))
                .andExpect(model().attribute("page", is(page)))
                .andExpect(model().attribute("currentPage", is(3)))
                .andExpect(model().attribute("totalPages", is(page.getTotalPages())))
                .andExpect(model().attribute("totalItems", is(page.getTotalElements())))
                .andExpect(model().attribute("users", hasSize(1)));
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser(role = UserRole.ROLE_ADMIN)
    public void saveUser() {
        UserCreationDTO dto = getCreationDTO();

        when(userServiceImpl.save(dto)).thenReturn(true);

        mvc.perform(post("/users/saveUser").flashAttr("user", dto))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/users/createUser?success"));

        verify(userServiceImpl, times(1)).save(dto);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser(role = UserRole.ROLE_ADMIN)
    public void updateUser() {
        UserUpdateDTO dto = getUpdateDTO();

        when(userServiceImpl.update(dto)).thenReturn(true);

        mvc.perform(post("/users/updateUser").flashAttr("user", dto))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/users/editUser?id=" + dto.getId()));

        verify(userServiceImpl, times(1)).update(dto);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser(role = UserRole.ROLE_ADMIN)
    public void createUser() {
        mvc.perform(get("/users/createUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user-form"))
                .andExpect(model().attribute("user", notNullValue()));
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser(role = UserRole.ROLE_ADMIN)
    public void editUser() {
        long id = 1L;

        when(userServiceImpl.getUserDTO(id)).thenReturn(getResponseDTO());

        mvc.perform(get("/users/editUser?id=" + id))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user-form"))
                .andExpect(model().attribute("user", notNullValue()));

        verify(userServiceImpl, times(1)).getUserDTO(id);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser(role = UserRole.ROLE_ADMIN)
    public void deleteUser() {
        long id = 1L;

        mvc.perform(post("/users/deleteUser?id=" + id))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/users/list"));

        verify(userServiceImpl, times(1)).deleteById(id, "testuser@mail.com");
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    public void userInfo() {
        long id = 1L;

        when(userServiceImpl.getUserDTO(id)).thenReturn(getResponseDTO());

        mvc.perform(get("/users/userInfo/" + id))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", notNullValue()))
                .andExpect(view().name("user/user-info"));

        verify(userServiceImpl, times(1)).getUserDTO(id);
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

    private UserResponseDTO getResponseDTO() {
        return UserResponseDTO.builder()
                .id(1L)
                .role(UserRole.ROLE_CLIENT)
                .email("testuser@mail.com")
                .firstName("Test")
                .lastName("User")
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
