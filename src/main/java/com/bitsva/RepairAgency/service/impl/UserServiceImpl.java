package com.bitsva.RepairAgency.service.impl;

import com.bitsva.RepairAgency.config.CustomUserDetails;
import com.bitsva.RepairAgency.dto.UserCreationDTO;
import com.bitsva.RepairAgency.dto.UserMapper;
import com.bitsva.RepairAgency.dto.UserResponseDTO;
import com.bitsva.RepairAgency.dto.UserUpdateDTO;
import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.repository.UserRepository;
import com.bitsva.RepairAgency.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<User> userList() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
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

    @Override
    public boolean update(UserUpdateDTO dto) {
        User existingUser = userRepository.findById(dto.getId()).orElse(null);
        if (dto.getPassword() != null) {
            dto.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        }
        User user = userMapper.mapUserUpdateDTOToUser(dto, existingUser);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean checkIfEmailExists(String email) {
        return userRepository.checkIfEmailExists(email) > 0;
    }

    @Override
    public boolean checkIfPhoneExists(String phone) {
        return userRepository.checkIfPhoneExists(phone) > 0;
    }

    @Override
    public boolean checkIfEmailLinkedToAnotherUser(String email, Long id) {
        return userRepository.checkIfEmailLinkedToAnotherUser(email, id) > 0;
    }

    @Override
    public boolean checkIfPhoneLinkedToAnotherUser(String phone, Long id) {
        return userRepository.checkIfPhoneLinkedToAnotherUser(phone, id) > 0;
    }

    @Transactional
    @Override
    public void updateBalance(CustomUserDetails loggedUser, long amountOfMoney) {
        userRepository.updateBalance(amountOfMoney, loggedUser.getId());
        loggedUser.setBalance(loggedUser.getBalance() + amountOfMoney);
    }

    @Override
    public void balanceChargeBack(long userId, long amountOfMoney) {
        userRepository.updateBalance(amountOfMoney, userId);
    }

    @Override
    public UserResponseDTO getUserDTO(long id) {
        User user = userRepository.findById(id).orElse(null);
        return userMapper.mapUserToUserResponseDTO(user);
    }

    @Override
    public void deleteById(long id, String email) {
        if (userRepository.checkUserAreNotSuperAdmin(id) == 0 && userRepository.checkIfEmailLinkedToAnotherUser(email, id) == 0) {
            userRepository.deleteById(id);
        }
    }

    @Override
    public Page<User> findPaginated(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        return userRepository.findAll(pageable);
    }

    @Override
    public void updateRating(float averageRating, Long id) {
        userRepository.updateRating(averageRating, id);
    }
}
