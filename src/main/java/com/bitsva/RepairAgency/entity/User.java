package com.bitsva.RepairAgency.entity;

import com.bitsva.RepairAgency.feature.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId(mutable = true)
    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private Long balance;

    private boolean isActive;

    @ManyToMany
    @JoinTable (
            name = "user_request",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "request_id", referencedColumnName = "id"))
    private List<RepairRequest> requests = new ArrayList<>();

    /*@ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();*/

    //private String password;

    //private List<Feedback> feedbacks;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    //private boolean isVerified


    //private boolean isReceivingMail;

}
