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
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private List<User> users = new ArrayList<>();

    @Column
    private String creationDate;

    @Column
    private String description;

    @Column
    private Long cost;

    @Column
    private Long depositedToPay;

    @Enumerated(EnumType.STRING)
    private RepairRequestCompletionStatus completionStatus;

    @Enumerated(EnumType.STRING)
    private RepairRequestPaymentStatus paymentStatus;

    /*@OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Feedback feedback;*/

    private boolean isHasFeedback;

    public User getClient() {
        return users.stream().filter(it -> it.getRole().equals(UserRole.ROLE_CLIENT)).findFirst().orElse(null);
    }

    public long getClientId() {
        return getClient().getId();
    }

    public String getClientName() {
        User client = getClient();
        return client == null ? "User not found" : client.getFullName();
    }

    public User getRepairer() {
        return users.stream().filter(it -> it.getRole().equals(UserRole.ROLE_REPAIRER)).findFirst().orElse(null);
    }

    public long getRepairerId() {
        return getRepairer().getId();
    }

    public String getRepairerName() {
        User repairer = getRepairer();
        return repairer == null ? "Not assigned" : repairer.getFullName();
    }

    public void setRepairer(User user) {
        users.removeIf(it -> it.getRole().equals(UserRole.ROLE_REPAIRER));
        users.add(user);
    }
}
