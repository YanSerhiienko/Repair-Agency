package com.bitsva.RepairAgency.service;

import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> usersList() {
        return userRepository.findAll();
    }

}


