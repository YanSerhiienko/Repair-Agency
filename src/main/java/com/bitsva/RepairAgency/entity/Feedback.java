package com.bitsva.RepairAgency.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "feedbacks")
public class Feedback {
    public Feedback() {
        this.feedbackDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String feedbackDate;

    private Long rating;

    @Size(min = 5, max = 50, message = "Size must be between 5 and 10000 characters")
    private String feedbackText;

    private Long requestId;

    private Long clientId;

    private Long repairerId;
}
