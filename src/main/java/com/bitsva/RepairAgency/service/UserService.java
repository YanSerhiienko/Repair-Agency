package com.bitsva.RepairAgency.service;

import com.bitsva.RepairAgency.config.CustomUserDetails;
import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.entity.user.*;
import com.bitsva.RepairAgency.feature.UserRole;
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
    private UserRepository<User> userRepository;
    @Autowired
    private UserRepository<Client> clientRepository;
    @Autowired
    private UserRepository<Manager> managerRepository;
    @Autowired
    private UserRepository<Repairer> repairerRepository;
    @Autowired
    private UserRepository<Admin> adminRepository;

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

        switch (user.getRole()) {
            case ROLE_CLIENT -> saveAsClient(user);
            case ROLE_MANAGER -> saveAsManager(user);
            case ROLE_REPAIRER -> saveAsRepairer(user);
            case ROLE_ADMIN -> saveAsAdmin(user);
        }

        //user.setRole(UserRole.CLIENT);
        //user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //userRepository.save(user);
        return true;
    }

    private void saveAsClient(User user) {
        Client client = new Client();
        client.setId(user.getId());
        client.setFirstName(user.getFirstName());
        client.setLastName(user.getLastName());
        client.setEmail(user.getEmail());
        client.setPhone(user.getPhone());
        if (!user.getPassword().equals("")) {
            client.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userRepository.save(client);
    }

    private void saveAsManager(User user) {
        Manager manager = new Manager();
        manager.setRole(UserRole.ROLE_MANAGER);
        manager.setId(user.getId());
        manager.setFirstName(user.getFirstName());
        manager.setLastName(user.getLastName());
        manager.setEmail(user.getEmail());
        manager.setPhone(user.getPhone());
        if (!user.getPassword().equals("")) {
            manager.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userRepository.save(manager);
    }

    private void saveAsRepairer(User user) {
        Repairer repairer = new Repairer();
        repairer.setRole(UserRole.ROLE_REPAIRER);
        repairer.setId(user.getId());
        repairer.setFirstName(user.getFirstName());
        repairer.setLastName(user.getLastName());
        repairer.setEmail(user.getEmail());
        repairer.setPhone(user.getPhone());
        if (!user.getPassword().equals("")) {
            repairer.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        //repairerRepository.save(repairer);
        userRepository.save(repairer);
    }

    private void saveAsAdmin(User user) {
        Admin admin = new Admin();
        admin.setRole(UserRole.ROLE_ADMIN);
        admin.setId(user.getId());
        admin.setFirstName(user.getFirstName());
        admin.setLastName(user.getLastName());
        admin.setEmail(user.getEmail());
        admin.setPhone(user.getPhone());
        if (!user.getPassword().equals("")) {
            admin.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userRepository.save(admin);
    }

    private void updateAsClient(User user) {
        Client client = new Client();
        client.setId(user.getId());
        client.setFirstName(user.getFirstName());
        client.setLastName(user.getLastName());
        client.setEmail(user.getEmail());
        client.setPhone(user.getPhone());
        if (!user.getPassword().equals("")) {
            client.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userRepository.save(client);
    }

    private void updateAsManager(User user) {
        Manager manager = new Manager();
        manager.setRole(UserRole.ROLE_MANAGER);
        manager.setId(user.getId());
        manager.setFirstName(user.getFirstName());
        manager.setLastName(user.getLastName());
        manager.setEmail(user.getEmail());
        manager.setPhone(user.getPhone());
        if (!user.getPassword().equals("")) {
            manager.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userRepository.save(manager);
    }

    private void updateAsRepairer(User user) {
        Repairer repairer = new Repairer();
        repairer.setRole(UserRole.ROLE_REPAIRER);
        repairer.setId(user.getId());
        repairer.setFirstName(user.getFirstName());
        repairer.setLastName(user.getLastName());
        repairer.setEmail(user.getEmail());
        repairer.setPhone(user.getPhone());
        if (!user.getPassword().equals("")) {
            repairer.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        //repairerRepository.save(repairer);
        userRepository.save(repairer);
    }

    private void updateAsAdmin(User user) {
        Admin admin = adminRepository.findAdminById(user.getId());
    }

    public boolean update(User user, String password) {
        /*if (!password.equals("")) {
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
        }*/
        //////////////////////////
        /*user.setPassword(password);
        switch (user.getRole()) {
            case ROLE_CLIENT:
                saveAsClient(user);
            case ROLE_MANAGER:
                saveAsManager(user);
            case ROLE_REPAIRER:
                saveAsRepairer(user);
            case ROLE_ADMIN:
                saveAsAdmin(user);
        }*/
        Long balance = 0L;
        Float rating = 0F;
        switch (user.getRole()) {
            case ROLE_CLIENT -> {
                balance = 0L;
                rating = null;
            }
            case ROLE_MANAGER -> {
                balance = null;
                rating = null;
            }
            case ROLE_REPAIRER -> {
                balance = null;
                rating = 0F;
            }
            case ROLE_ADMIN -> {
                balance = null;
                rating = null;
            }
        }

        userRepository.updateUser(user.getRole().label, user.getId(), user.getEmail(),
                user.getFirstName(), user.getLastName(), user.getPhone(),
                user.getRole(), balance, rating);

        return true;
    }

    @Transactional
    public void updateBalance(CustomUserDetails loggedUser, long amountOfMoney) {
        userRepository.updateBalance(amountOfMoney, loggedUser.getId());
        //loggedUser.setBalance(loggedUser.getBalance() + amountOfMoney);
    }

    public User getById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Repairer getRepairerById(long id) {
        return userRepository.findRepairerById(id);
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


