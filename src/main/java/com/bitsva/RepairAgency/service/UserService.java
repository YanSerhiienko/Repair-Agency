package com.bitsva.RepairAgency.service;

import com.bitsva.RepairAgency.config.CustomUserDetails;
import com.bitsva.RepairAgency.dto.UserCreationDTO;
import com.bitsva.RepairAgency.dto.UserResponseDTO;
import com.bitsva.RepairAgency.dto.UserUpdateDTO;
import com.bitsva.RepairAgency.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    public List<User> userList();

    public User findUserByEmail(String email);

    public boolean save(UserCreationDTO dto);

    public boolean update(UserUpdateDTO dto);

    public boolean checkIfEmailExists(String email);

    public boolean checkIfPhoneExists(String phone);

    public boolean checkIfEmailLinkedToAnotherUser(String email, Long id);

    public boolean checkIfPhoneLinkedToAnotherUser(String phone, Long id);

    public void updateBalance(CustomUserDetails loggedUser, long amountOfMoney);

    public void balanceChargeBack(long userId, long amountOfMoney);

    public UserResponseDTO getUserDTO(long id);

    public void deleteById(long id, String email);

    public Page<User> findPaginated(int page, int size);

    public void updateRating(float averageRating, Long id);
}
