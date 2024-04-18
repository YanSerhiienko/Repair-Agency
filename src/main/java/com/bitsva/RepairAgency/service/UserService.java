package com.bitsva.RepairAgency.service;

import com.bitsva.RepairAgency.config.CustomUserDetails;
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
public class UserService { // implements UserDetailsService
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<User> usersList() {
        return userRepository.findAll();
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean save(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            return false;
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean update(User user, String password) {
        if (!password.equals("")) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } else {
            User existingUser = userRepository.findById(user.getId()).orElse(null);
            System.out.println("existingUser.BEFORE UPDATE = " + existingUser);
            existingUser.setEmail(user.getEmail());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setPhone(user.getPhone());
            existingUser.setRole(user.getRole());
            userRepository.save(existingUser);
            System.out.println("existingUser.AFTER UPDATE = " + existingUser);
        }
        return true;
    }

    @Transactional
    public void updateBalance(CustomUserDetails loggedUser, long amountOfMoney) {
        userRepository.updateBalance(amountOfMoney, loggedUser.getId());
        loggedUser.setBalance(loggedUser.getBalance() + amountOfMoney);
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

    public void changeAccountStatus(long id, boolean isEnabled) {
        User user = getById(id);
        /*boolean aBoolean = Boolean.getBoolean(isEnabled);
        System.out.println("aBoolean = " + aBoolean);*/
        user.setEnabled(isEnabled);
        userRepository.save(user);
    }

    /*@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRole()));
    }

    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(UserRole role) {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }*/
}


