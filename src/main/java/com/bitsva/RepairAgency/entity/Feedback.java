package com.bitsva.RepairAgency.entity;

import com.bitsva.RepairAgency.feature.RepairRequestCompletionStatus;
import com.bitsva.RepairAgency.feature.RepairRequestPaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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

    //TODO check for errors after replacement of Integer
    private Long rating;

    //@Size(min = 5, max = 50, message = "Size must be between 5 and 100000 characters")
    private String feedbackText;

    private Long requestId;

    //TODO cleanup
    //private RepairRequest request;

    /*@ManyToMany
    private List<User> users = new ArrayList<>();*/

    private Long clientId;

    private Long repairerId;

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", feedbackDate='" + feedbackDate + '\'' +
                ", rating=" + rating +
                ", feedbackText='" + feedbackText + '\'' +
                ", requestId=" + requestId +
                ", clientId=" + clientId +
                ", repairerId=" + repairerId +
                '}';
    }
}
