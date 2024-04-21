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

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class FeedbackRepositoryTests {
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Test
    public void FeedbackRepository_Save_ReturnSavedFeedback() {
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
    public void FeedbackRepository_GetAll_ReturnMoreThenOneFeedback() {
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
    public void FeedbackRepository_FindById_ReturnFeedbackNotNull() {
        Feedback feedback = Feedback.builder()
                .feedbackText("Good job!")
                .feedbackDate(LocalDate.now().toString())
                .rating(5L)
                .build();

        feedbackRepository.save(feedback);

        Feedback feedbackById = feedbackRepository.findById(feedback.getId()).get();

        Assertions.assertThat(feedbackById).isNotNull();
    }

    //TODO is it really needed
    @Test
    public void FeedbackRepository_UpdateFeedback_ReturnFeedbackNotNull() {
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
    public void FeedbackRepository_FeedbackDelete_ReturnFeedbackIsEmpty() {
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
