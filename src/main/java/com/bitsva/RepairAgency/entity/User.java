package com.bitsva.RepairAgency.entity;

import com.bitsva.RepairAgency.feature.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NaturalId(mutable = true)
    @Email(message = "Should be in format of email")
    private String email;


    @Size(min = 2, max = 50, message = "Size must be between 5 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name can contain only latin symbols")
    private String firstName;

    @Size(min = 2, max = 50, message = "Size must be between 5 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name can contain only latin symbols")
    private String lastName;

    @Column(unique = true)
    @Size(min = 10, max = 10, message = "Should contain 10 symbols")
    @Pattern(regexp = "^[0-9]+$", message = "Should be in format 0631234567")
    private String phone;

    private Long balance;

    private Float rating;

    private boolean isEnabled;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Size(min = 8, max = 100, message = "Size must be between 8 and 100 characters")
    private String password;

    public String getFullName() {
        return lastName + " " + firstName;
    }

    public User() {
        this.balance = 0L;
        this.isEnabled = true;
        this.role = UserRole.ROLE_CLIENT;
    }

    public String getRating() {
        return rating == null ? "Repairer has no rating yet" : rating.toString();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", isEnabled=" + isEnabled +
                ", role=" + role +
                ", password='" + password + '\'' +
                '}';
    }
}
