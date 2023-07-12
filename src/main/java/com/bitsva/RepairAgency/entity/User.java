package com.bitsva.RepairAgency.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "\"user\"")
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

    @ManyToMany //(cascade = CascadeType.ALL)
    private List<RepairRequest> requests = new ArrayList<>();
    //private String password;

    //private List<Feedback> feedbacks;

    //@Enumerated(EnumType.STRING)
    //private UserRole role;

    //private boolean isVerified


    //private boolean isReceivingMail;

}
