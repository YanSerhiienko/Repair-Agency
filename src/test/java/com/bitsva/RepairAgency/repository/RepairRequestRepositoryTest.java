package com.bitsva.RepairAgency.repository;

import com.bitsva.RepairAgency.entity.RepairRequest;
import com.bitsva.RepairAgency.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RepairRequestRepositoryTest {
    @Autowired
    private RepairRequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void searchByClientId() {
        User client = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("Client")
                .email("client@mail.com")
                .build();
        User repairer = User.builder()
                .id(2L)
                .firstName("Test")
                .lastName("Repairer")
                .email("repirer@mail.com")
                .build();

        long id = 1L;
        PageRequest pageable = PageRequest.of(0, 1);

        RepairRequest request = getRequest();
        request.setClient(client);
        request.setRepairer(repairer);

        userRepository.save(client);
        userRepository.save(repairer);
        requestRepository.save(request);

        Page<RepairRequest> repairRequests = requestRepository.searchByClientId(id, pageable);
        RepairRequest pagingRequest = repairRequests.getContent().get(0);

        assertEquals(1, repairRequests.getTotalElements());
        assertEquals(request, pagingRequest);
    }

    @Test
    public void searchByRepairerId() {
        User client = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("Client")
                .email("client@mail.com")
                .build();
        User repairer = User.builder()
                .id(2L)
                .firstName("Test")
                .lastName("Repairer")
                .email("repirer@mail.com")
                .build();

        long id = 2L;
        PageRequest pageable = PageRequest.of(0, 1);

        RepairRequest request = getRequest();
        request.setClient(client);
        request.setRepairer(repairer);

        userRepository.save(client);
        userRepository.save(repairer);
        requestRepository.save(request);

        Page<RepairRequest> repairRequests = requestRepository.searchByRepairerId(id, pageable);
        RepairRequest pagingRequest = repairRequests.getContent().get(0);

        assertEquals(1, repairRequests.getTotalElements());
        assertEquals(request, pagingRequest);
    }

    @Test
    public void save_ReturnSavedRequest() {
        RepairRequest request = getRequest();

        RepairRequest savedRequest = requestRepository.save(request);

        Assertions.assertThat(savedRequest).isNotNull();
        Assertions.assertThat(savedRequest.getId()).isGreaterThan(0);
    }

    @Test
    public void getAll_ReturnMoreThenOneRequest() {
        RepairRequest request1 = getRequest();

        RepairRequest request2 = getRequest();
        request2.setDescription("fix it again please");

        requestRepository.save(request1);
        requestRepository.save(request2);

        List<RepairRequest> requestList = requestRepository.findAll();

        Assertions.assertThat(requestList).isNotNull();
        Assertions.assertThat(requestList.size()).isEqualTo(2);
    }

    @Test
    public void findById_ReturnRequestNotNull() {
        RepairRequest request = getRequest();

        requestRepository.save(request);

        RepairRequest requestById = requestRepository.findById(request.getId()).get();

        Assertions.assertThat(requestById).isNotNull();
    }

    @Test
    public void update_ReturnRequestNotNull() {
        RepairRequest request = getRequest();

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
    public void delete_ReturnRequestIsEmpty() {
        RepairRequest request = getRequest();

        requestRepository.save(request);

        requestRepository.deleteById(request.getId());
        Optional<RepairRequest> requestById = requestRepository.findById(request.getId());

        Assertions.assertThat(requestById).isEmpty();
    }

    private RepairRequest getRequest() {
        return RepairRequest.builder()
                .description("Fix it please")
                .creationDate(LocalDate.now().toString())
                .cost(100L)
                .build();
    }
}
