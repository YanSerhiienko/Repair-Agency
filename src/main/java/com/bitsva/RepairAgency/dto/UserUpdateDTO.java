package com.bitsva.RepairAgency.dto;

import com.bitsva.RepairAgency.feature.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    private Long id;

    @Email(message = "Should be in format of email")
    private String email;

    @Size(min = 2, max = 50, message = "Size must be between 5 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name can contain only latin symbols")
    private String firstName;

    @Size(min = 2, max = 50, message = "Size must be between 5 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name can contain only latin symbols")
    private String lastName;

    @Size(min = 10, max = 10, message = "Should contain 10 symbols")
    @Pattern(regexp = "^[0-9]+$", message = "Should be in format 0631234567")
    private String phone;

    private UserRole role;

    @Size(min = 8, max = 100, message = "Size must be between 8 and 100 characters")
    private String password;
}
