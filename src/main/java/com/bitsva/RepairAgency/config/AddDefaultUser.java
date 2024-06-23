package com.bitsva.RepairAgency.config;

import com.bitsva.RepairAgency.dto.UserCreationDTO;
import com.bitsva.RepairAgency.feature.UserRole;
import com.bitsva.RepairAgency.repository.UserRepository;
import com.bitsva.RepairAgency.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddDefaultUser {
    private final UserService userService;
    private final UserRepository userRepository;
    @Value("${super.admin.email}")
    private String email;
    @Value("${super.admin.password}")
    private String password;
    @Value("${super.admin.phone}")
    private String phone;

    @EventListener(ApplicationReadyEvent.class)
    public void appReady() {
        if (userRepository.findByRole(UserRole.ROLE_SUPER_ADMIN) == null) {
            addSuperAdminUser();
        }
    }

    private void addSuperAdminUser() {
        UserCreationDTO adminUser = UserCreationDTO.builder()
                .role(UserRole.ROLE_SUPER_ADMIN)
                .firstName("Super")
                .lastName("Admin")
                .balance(0L)
                .rating(0f)
                .email(email)
                .password(password)
                .phone(phone)
                .build();
        userService.save(adminUser);
    }
}
