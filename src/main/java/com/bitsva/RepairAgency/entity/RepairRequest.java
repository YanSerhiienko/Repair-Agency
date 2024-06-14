package com.bitsva.RepairAgency.entity;

import com.bitsva.RepairAgency.feature.RepairRequestCompletionStatus;
import com.bitsva.RepairAgency.feature.RepairRequestPaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "requests")
public class RepairRequest {
    public RepairRequest() {
        this.cost = 0L;
        this.depositedToPay = 0L;
        this.completionStatus = RepairRequestCompletionStatus.NOT_STARTED;
        this.paymentStatus = RepairRequestPaymentStatus.AWAITING_PAYMENT;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private User client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repairer_id")
    private User repairer;

    private String creationDate;

    @Size(min = 5, max = 50, message = "Size must be between 10 and 10000 characters")
    private String description;

    private Long cost;

    private Long depositedToPay;

    @Enumerated(EnumType.STRING)
    private RepairRequestCompletionStatus completionStatus;

    @Enumerated(EnumType.STRING)
    private RepairRequestPaymentStatus paymentStatus;

    private boolean isHasFeedback;

    public User getClient() {
        return client;
    }

    public long getClientId() {
        return getClient().getId();
    }

    public String getClientName() {
        User client = getClient();
        return client == null ? "User not found" : client.getFullName();
    }

    public User getRepairer() {
        return repairer;
    }

    public long getRepairerId() {
        return getRepairer().getId();
    }

    public String getRepairerName() {
        User repairer = getRepairer();
        return repairer == null ? "Not assigned" : repairer.getFullName();
    }

    public void setRepairer(User repairer) {
        this.repairer = repairer;
    }
}
