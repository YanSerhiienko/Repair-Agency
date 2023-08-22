package com.bitsva.RepairAgency.entity;

import com.bitsva.RepairAgency.feature.UserRole;
import jakarta.persistence.*;
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
    private String firstName;
    @Column
    private String lastName;
    @Column
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
