package com.bitsva.RepairAgency.service.impl;

import com.bitsva.RepairAgency.config.CustomUserDetails;
import com.bitsva.RepairAgency.dto.UserCreationDTO;
import com.bitsva.RepairAgency.dto.UserResponseDTO;
import com.bitsva.RepairAgency.dto.UserUpdateDTO;
import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.feature.UserRole;
import com.bitsva.RepairAgency.repository.UserRepository;
import com.bitsva.RepairAgency.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    UserService userService;
    @MockBean
    UserRepository userRepository;

    @Test
    public void usersList() {
        User user = getUser();
        List<User> userList = List.of(user);

        when(userRepository.findAll()).thenReturn(userList);
        List<User> users = userService.userList();

        assertEquals(userList, users);
    }

    @Test
    public void findUserByEmail() {
        String email = "testuser@mail.com";
        User user = getUser();

        when(userRepository.findByEmail(email)).thenReturn(user);
        User userByEmail = userService.findUserByEmail(email);

        assertEquals(user, userByEmail);
    }

    @Test
    public void save_ReturnTrue() {
        UserCreationDTO dto = getCreationDTO();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(null);

        boolean isSaved = userService.save(dto);
        assertTrue(isSaved);
    }

    @Test
    public void save_ReturnFalse() {
        UserCreationDTO dto = getCreationDTO();
        User user = getUser();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(user);

        boolean isSaved = userService.save(dto);
        assertFalse(isSaved);
    }

    @Test
    public void update() {
        UserUpdateDTO dto = getUpdateDTO();
        User user = getUser();

        assertNotEquals(dto.getEmail(), user.getEmail());

        when(userRepository.findById(dto.getId())).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        boolean isUpdated = userService.update(dto);

        assertTrue(isUpdated);
        verify(userRepository, times(1)).findById(dto.getId());
        verify(userRepository, times(1)).save(user);
        assertEquals(dto.getEmail(), user.getEmail());
    }

    @Test
    public void checkIfEmailExists() {
        User user = getUser();
        when(userRepository.checkIfEmailExists(user.getEmail())).thenReturn(1);
        boolean isEmailExists = userService.checkIfEmailExists(user.getEmail());
        assertTrue(isEmailExists);
    }

    @Test
    public void checkIfPhoneExists() {
        User user = getUser();
        when(userRepository.checkIfPhoneExists(user.getPhone())).thenReturn(1);
        boolean checkIfPhoneExists = userService.checkIfPhoneExists(user.getPhone());
        assertTrue(checkIfPhoneExists);
    }

    @Test
    public void checkIfEmailLinkedToAnotherUser() {
        User user = getUser();
        when(userRepository.checkIfEmailLinkedToAnotherUser(user.getEmail(), user.getId())).thenReturn(1);
        boolean isEmailLinkedToUser = userService.checkIfEmailLinkedToAnotherUser(user.getEmail(), user.getId());
        assertTrue(isEmailLinkedToUser);
    }

    @Test
    public void checkIfPhoneLinkedToAnotherUser() {
        User user = getUser();
        when(userRepository.checkIfPhoneLinkedToAnotherUser(user.getPhone(), user.getId())).thenReturn(1);
        boolean isPhoneLinkedToUser = userService.checkIfPhoneLinkedToAnotherUser(user.getPhone(), user.getId());
        assertTrue(isPhoneLinkedToUser);
    }

    @Test
    public void updateBalance() {
        long id = 1L;
        long amountOfMoney = 100L;
        CustomUserDetails userDetails = getUserDetails();

        doNothing().when(userRepository).updateBalance(amountOfMoney, id);

        userService.updateBalance(userDetails, amountOfMoney);

        verify(userRepository, times(1)).updateBalance(amountOfMoney, id);
        assertEquals(amountOfMoney, userDetails.getBalance());
    }

    @Test
    public void balanceChargeBack() {
        long id = 1L;
        long amountOfMoney = 100L;

        doNothing().when(userRepository).updateBalance(amountOfMoney, id);

        userService.balanceChargeBack(id, amountOfMoney);

        verify(userRepository, times(1)).updateBalance(amountOfMoney, id);
    }

    @Test
    public void getUserDTO() {
        long id = 1L;
        User user = getUser();

        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(user));

        UserResponseDTO userDTO = userService.getUserDTO(id);

        assertEquals(user.getEmail(), userDTO.getEmail());
    }

    @Test
    public void deleteById() {
        long id = 1L;
        String email = "testuser@mail.com";

        when(userRepository.checkUserAreNotSuperAdmin(id)).thenReturn(0);
        when(userRepository.checkIfEmailLinkedToAnotherUser(email, id)).thenReturn(1);
        doNothing().when(userRepository).deleteById(id);

        userService.deleteById(id, email);

        verify(userRepository, times(1)).checkUserAreNotSuperAdmin(id);
        verify(userRepository, times(1)).checkIfEmailLinkedToAnotherUser(email, id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    public void findPaginated() {
        int pageNumber = 1;
        int pageSize = 5;
        PageRequest pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<User> page = new PageImpl<>(List.of(getUser()));

        when(userRepository.findAll(pageable)).thenReturn(page);

        Page<User> paginated = userService.findPaginated(pageNumber, pageSize);

        assertEquals(page, paginated);
    }

    @Test
    public void updateRating() {
        long id = 1L;
        float rating = 5.0f;

        doNothing().when(userRepository).updateRating(rating, id);

        userService.updateRating(rating, id);

        verify(userRepository, times(1)).updateRating(rating, id);
    }

    private CustomUserDetails getUserDetails() {
        User user = User.builder()
                .id(1L)
                .role(UserRole.ROLE_CLIENT)
                .email("testuser@mail.com")
                .firstName("Name")
                .lastName("Surname")
                .balance(0L)
                .build();

        return new CustomUserDetails(user);
    }

    private User getUser() {
        return User.builder()
                .role(UserRole.ROLE_CLIENT)
                .balance(0L)
                .email("testuser@mail.com")
                .firstName("Test")
                .lastName("User")
                .phone("0991234567")
                .password("password")
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

    private UserUpdateDTO getUpdateDTO() {
        return UserUpdateDTO.builder()
                .id(1L)
                .role(UserRole.ROLE_CLIENT)
                .email("updateduser@mail.com")
                .firstName("Updated")
                .lastName("User")
                .phone("0991234567")
                .password("password")
                .build();
    }
}
