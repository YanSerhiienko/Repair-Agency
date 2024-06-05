package com.bitsva.RepairAgency.service;

import com.bitsva.RepairAgency.config.CustomUserDetails;
import com.bitsva.RepairAgency.dto.UserCreationDTO;
import com.bitsva.RepairAgency.dto.UserMapper;
import com.bitsva.RepairAgency.dto.UserResponseDTO;
import com.bitsva.RepairAgency.dto.UserUpdateDTO;
import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<User> usersList() {
        return userRepository.findAll();
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean save(UserCreationDTO dto) {
        User existingUser = userRepository.findByEmail(dto.getEmail());

        if (existingUser != null) {
            return false;
        }

        dto.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        User user = userMapper.mapUserCreationDTOToUser(dto);
        userRepository.save(user);
        return true;
    }

    public boolean update(UserUpdateDTO dto) {
        User existingUser = userRepository.findById(dto.getId()).orElse(null);
        if (dto.getPassword() != null) {
            dto.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        }
        User user = userMapper.mapUserUpdateDTOToUser(dto, existingUser);
        userRepository.save(user);
        return true;
    }

    public boolean checkIfEmailExists(String email) {
        return userRepository.checkIfEmailExists(email) > 0;
    }

    public boolean checkIfPhoneExists(String phone) {
        return userRepository.checkIfPhoneExists(phone) > 0;
    }

    public boolean checkEmailForExistingUser(String email, Long id) {
        return userRepository.checkEmailForExistingUser(email, id) > 0;
    }

    public boolean checkPhoneForExistingUser(String phone, Long id) {
        return userRepository.checkPhoneForExistingUser(phone, id) > 0;
    }

    @Transactional
    public void updateBalance(CustomUserDetails loggedUser, long amountOfMoney) {
        userRepository.updateBalance(amountOfMoney, loggedUser.getId());
        loggedUser.setBalance(loggedUser.getBalance() + amountOfMoney);
    }

    public void balanceChargeBack(long userId, long amountOfMoney) {
        userRepository.updateBalance(amountOfMoney, userId);
    }

    public UserResponseDTO getUserDTO(long id) {
        User user = userRepository.findById(id).orElse(null);
        return userMapper.mapUserToUserResponseDTO(user);
    }

    public void deleteById(long id, String email) {
        if (userRepository.checkUseRole(id) == 0 && userRepository.checkEmailForExistingUser(email, id) == 0) {
            userRepository.deleteById(id);
        }
    }

    public Page<User> findPaginated(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        return userRepository.findAll(pageable);
    }

    public void updateRating(float averageRating, Long id) {
        userRepository.updateRating(averageRating, id);
    }
}


