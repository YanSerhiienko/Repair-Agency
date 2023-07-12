package com.bitsva.RepairAgency.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String feedbackDate;

    private Integer rating;

    private String description;

    private Long requestId;

    private String clientId;

    private String repairerId;
}
