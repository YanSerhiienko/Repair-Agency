package com.bitsva.RepairAgency.service;

import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> usersList() {
        return userRepository.findAll();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void update(User user) {
        userRepository.flush();
    }

    public User getById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    public Page<User> findPaginated(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        return userRepository.findAll(pageable);
    }
}


