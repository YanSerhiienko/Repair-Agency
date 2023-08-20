package com.bitsva.RepairAgency.entity;

import com.bitsva.RepairAgency.feature.RepairRequestCompletionStatus;
import com.bitsva.RepairAgency.feature.RepairRequestPaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "feedback")
public class Feedback {
    public Feedback() {
        this.feedbackDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String feedbackDate;

    private Integer rating;

    private String feedbackText;

    private Long requestId;

    /*@ManyToMany
    private List<User> users = new ArrayList<>();*/

    private Long clientId;

    private Long repairerId;
}
