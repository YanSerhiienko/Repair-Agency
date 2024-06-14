package com.bitsva.RepairAgency.repository;

import com.bitsva.RepairAgency.entity.Feedback;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class FeedbackRepositoryTest {
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Test
    public void averageRepairerRating() {
        Feedback feedback1 = Feedback.builder()
                .repairerId(1L)
                .rating(5L)
                .build();
        Feedback feedback2 = Feedback.builder()
                .repairerId(1L)
                .rating(4L)
                .build();
        Feedback feedback3 = Feedback.builder()
                .repairerId(1L)
                .rating(3L)
                .build();

        feedbackRepository.save(feedback1);
        feedbackRepository.save(feedback2);
        feedbackRepository.save(feedback3);

        float repairerRating = feedbackRepository.averageRepairerRating(1L);
        assertEquals(4, repairerRating);
    }

    @Test
    public void findByRequestId() {
        Feedback feedback = Feedback.builder()
                .requestId(1L)
                .build();

        feedbackRepository.save(feedback);

        Feedback byRequestId = feedbackRepository.findByRequestId(1L);

        assertEquals(feedback, byRequestId);
    }

    @Test
    public void save_ReturnSavedFeedback() {
        Feedback feedback = Feedback.builder()
                .feedbackText("Good job!")
                .feedbackDate(LocalDate.now().toString())
                .rating(5L)
                .build();

        Feedback savedFeedback = feedbackRepository.save(feedback);

        Assertions.assertThat(savedFeedback).isNotNull();
        Assertions.assertThat(savedFeedback.getId()).isGreaterThan(0);
    }

    @Test
    public void getAll_ReturnMoreThenOneFeedback() {
        Feedback feedback1 = Feedback.builder()
                .feedbackText("Good job!")
                .feedbackDate(LocalDate.now().toString())
                .rating(5L)
                .build();

        Feedback feedback2 = Feedback.builder()
                .feedbackText("Nice job!")
                .feedbackDate(LocalDate.now().toString())
                .rating(5L)
                .build();

        feedbackRepository.save(feedback1);
        feedbackRepository.save(feedback2);

        List<Feedback> feedbackList = feedbackRepository.findAll();

        Assertions.assertThat(feedbackList).isNotNull();
        Assertions.assertThat(feedbackList.size()).isEqualTo(2);
    }

    @Test
    public void findById_ReturnFeedbackNotNull() {
        Feedback feedback = Feedback.builder()
                .feedbackText("Good job!")
                .feedbackDate(LocalDate.now().toString())
                .rating(5L)
                .build();

        feedbackRepository.save(feedback);

        Feedback feedbackById = feedbackRepository.findById(feedback.getId()).get();

        Assertions.assertThat(feedbackById).isNotNull();
    }

    @Test
    public void update_ReturnFeedbackNotNull() {
        Feedback feedback = Feedback.builder()
                .feedbackText("Good job!")
                .feedbackDate(LocalDate.now().toString())
                .rating(5L)
                .build();

        feedbackRepository.save(feedback);

        Feedback savedFeedback = feedbackRepository.findById(feedback.getId()).get();
        savedFeedback.setFeedbackText("Really good job!");
        savedFeedback.setFeedbackDate(LocalDate.now().toString());

        Feedback updatedFeedback = feedbackRepository.save(savedFeedback);

        Assertions.assertThat(updatedFeedback.getFeedbackText()).isNotNull();
        Assertions.assertThat(updatedFeedback.getFeedbackDate()).isNotNull();
    }

    @Test
    public void delete_ReturnFeedbackIsEmpty() {
        Feedback feedback = Feedback.builder()
                .feedbackText("Good job!")
                .feedbackDate(LocalDate.now().toString())
                .rating(5L)
                .build();

        feedbackRepository.save(feedback);

        feedbackRepository.deleteById(feedback.getId());
        Optional<Feedback> feedbackById = feedbackRepository.findById(feedback.getId());

        Assertions.assertThat(feedbackById).isEmpty();
    }
}
