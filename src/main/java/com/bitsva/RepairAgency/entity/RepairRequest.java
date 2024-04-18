package com.bitsva.RepairAgency.entity;

import com.bitsva.RepairAgency.entity.user.Client;
import com.bitsva.RepairAgency.entity.user.Repairer;
import com.bitsva.RepairAgency.feature.RepairRequestCompletionStatus;
import com.bitsva.RepairAgency.feature.RepairRequestPaymentStatus;
import com.bitsva.RepairAgency.feature.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "requests")
public class RepairRequest {
    //@Autowired
    //private UserRepository<Repairer> repairerRepository;

    public RepairRequest() {
        this.cost = 0L;
        this.depositedToPay = 0L;
        this.completionStatus = RepairRequestCompletionStatus.NOT_STARTED;
        this.paymentStatus = RepairRequestPaymentStatus.AWAITING_PAYMENT;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private List<User> users = new ArrayList<>();

    private Long repairerId;

    @Column
    private String creationDate;

    /*@Column
    @Size(min = 5, max = 50, message = "Size must be between 10 and 10000 characters")
    private String title;*/

    @Column
    @Size(min = 5, max = 50, message = "Size must be between 10 and 10000 characters")
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

    //@OneToOne(fetch = FetchType.LAZY)
    //private Feedback feedback;

    private boolean isHasFeedback;

    //////////////////////
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
