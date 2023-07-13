package com.bitsva.RepairAgency.entity;

import com.bitsva.RepairAgency.feature.RepairRequestCompletionStatus;
import com.bitsva.RepairAgency.feature.RepairRequestPaymentStatus;
import com.bitsva.RepairAgency.feature.UserRole;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "requests")
public class RepairRequest {
    public RepairRequest() {
        this.creationDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.cost = 0L;
        this.completionStatus = RepairRequestCompletionStatus.NOT_STARTED;
        this.paymentStatus = RepairRequestPaymentStatus.AWAITING_PAYMENT;
        this.repairer = null;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String creationDate;

    private String description;

    @ManyToOne
    private User client;

    private String repairer;

    /*@ManyToMany
    @JoinTable (
            name = "user_request",
            joinColumns = @JoinColumn(name = "request_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> users = new ArrayList<>();*/

    private Long cost;

    @Enumerated(EnumType.STRING)
    private RepairRequestCompletionStatus completionStatus;

    @Enumerated(EnumType.STRING)
    private RepairRequestPaymentStatus paymentStatus;

    /*public User getClient() {
        return users.stream().filter(it -> it.getRole().equals(UserRole.ROLE_CLIENT)).findFirst().orElse(null);
    }

    public User getRepairer() {
        return users.stream().filter(it -> it.getRole().equals(UserRole.ROLE_REPAIRER)).findFirst().orElse(null);
    }

    public void setRepairer(User user) {
        users.removeIf(it -> it.getRole().equals(UserRole.ROLE_REPAIRER));
        users.add(user);
    }*/
}
