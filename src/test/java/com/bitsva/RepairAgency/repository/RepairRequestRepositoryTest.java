package com.bitsva.RepairAgency.repository;

import com.bitsva.RepairAgency.entity.RepairRequest;
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
public class RepairRequestRepositoryTest {
    @Autowired
    private RepairRequestRepository requestRepository;

    @Test
    public void RequestRepository_Save_ReturnSavedRequest() {
        RepairRequest request = RepairRequest.builder()
                .description("Fix it please")
                .creationDate(LocalDate.now().toString())
                .cost(100L)
                .build();

        RepairRequest savedRequest = requestRepository.save(request);

        Assertions.assertThat(savedRequest).isNotNull();
        Assertions.assertThat(savedRequest.getId()).isGreaterThan(0);
    }

    @Test
    public void RequestRepository_GetAll_ReturnMoreThenOneRequest() {
        RepairRequest request1 = RepairRequest.builder()
                .description("Fix it please")
                .creationDate(LocalDate.now().toString())
                .cost(100L)
                .build();

        RepairRequest request2 = RepairRequest.builder()
                .description("Fix it again please")
                .creationDate(LocalDate.now().toString())
                .cost(100L)
                .build();

        requestRepository.save(request1);
        requestRepository.save(request2);

        List<RepairRequest> requestList = requestRepository.findAll();

        Assertions.assertThat(requestList).isNotNull();
        Assertions.assertThat(requestList.size()).isEqualTo(2);
    }

    @Test
    public void RequestRepository_FindById_ReturnRequestNotNull() {
        RepairRequest request = RepairRequest.builder()
                .description("Fix it please")
                .creationDate(LocalDate.now().toString())
                .cost(100L)
                .build();

        requestRepository.save(request);

        RepairRequest requestById = requestRepository.findById(request.getId()).get();

        Assertions.assertThat(requestById).isNotNull();
    }

    @Test
    public void RequestRepository_UpdateRequest_ReturnRequestNotNull() {
        RepairRequest request = RepairRequest.builder()
                .description("Fix it please")
                .creationDate(LocalDate.now().toString())
                .cost(100L)
                .build();

        requestRepository.save(request);

        RepairRequest savedRequest = requestRepository.findById(request.getId()).get();
        savedRequest.setDescription("Count on your skill");
        savedRequest.setCreationDate(LocalDate.now().toString());
        savedRequest.setCost(150L);

        RepairRequest updatedRequest = requestRepository.save(savedRequest);

        Assertions.assertThat(updatedRequest.getDescription()).isNotNull();
        Assertions.assertThat(updatedRequest.getCreationDate()).isNotNull();
        Assertions.assertThat(updatedRequest.getCost()).isNotNull();
    }

    @Test
    public void RequestRepository_RequestDelete_ReturnRequestIsEmpty() {
        RepairRequest request = RepairRequest.builder()
                .description("Fix it please")
                .creationDate(LocalDate.now().toString())
                .cost(100L)
                .build();

        requestRepository.save(request);

        requestRepository.deleteById(request.getId());
        Optional<RepairRequest> requestById = requestRepository.findById(request.getId());

        Assertions.assertThat(requestById).isEmpty();
    }
}
