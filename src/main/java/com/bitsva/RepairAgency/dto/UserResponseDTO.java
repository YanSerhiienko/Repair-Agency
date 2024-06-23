package com.bitsva.RepairAgency.dto;

import com.bitsva.RepairAgency.feature.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Long balance;
    private String phone;
    private Float rating;
    private UserRole role;

    public String getFullName() {
        return lastName + " " + firstName;
    }

    public String getRating() {
        return rating == null || rating == 0 ? "Repairer has no rating yet" : rating.toString();
    }
}
