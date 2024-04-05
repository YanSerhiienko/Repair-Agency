package com.bitsva.RepairAgency.entity;

import com.bitsva.RepairAgency.feature.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@Entity
@Table(name = "users")
public class User  { //implements UserDetails
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId(mutable = true)
    private String email;

    @Column
    @Size(min = 5, max = 50, message = "Size must be between 5 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Name can contain only latin symbols")
    private String firstName;

    @Column
    @Size(min = 5, max = 50, message = "Size must be between 5 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name can contain only latin symbols")
    private String lastName;

    @Column
    @Size(min = 10, max = 10, message = "number should be in format 0631234567")
    @Pattern(regexp = "^[0-9]+$", message = "number should be in format 0631234567")
    private String phone;

    @Column
    private Long balance;

    @Column
    private Float rating;

    @ManyToMany(mappedBy = "users")
    private List<RepairRequest> requests = new ArrayList<>();

    @Column
    private boolean isEnabled;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column
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



  /*  @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isEnabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }*/
//private List<Feedback> feedbacks;

    //private boolean isVerified

    //private boolean isReceivingMail;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", balance=" + balance +
                ", isEnabled=" + isEnabled +
                ", role=" + role +
                ", password='" + password + '\'' +
                '}';
    }

}
