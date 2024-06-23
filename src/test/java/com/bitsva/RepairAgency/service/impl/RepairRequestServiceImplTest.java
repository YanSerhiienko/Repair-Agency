package com.bitsva.RepairAgency.service.impl;

import com.bitsva.RepairAgency.config.CustomUserDetails;
import com.bitsva.RepairAgency.entity.RepairRequest;
import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.feature.RepairRequestCompletionStatus;
import com.bitsva.RepairAgency.feature.RepairRequestPaymentStatus;
import com.bitsva.RepairAgency.feature.UserRole;
import com.bitsva.RepairAgency.repository.RepairRequestRepository;
import com.bitsva.RepairAgency.repository.UserRepository;
import com.bitsva.RepairAgency.service.RepairRequestService;
import com.bitsva.RepairAgency.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class RepairRequestServiceImplTest {
    @Autowired
    RepairRequestService requestService;
    @MockBean
    RepairRequestRepository requestRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    UserService userService;

    @Test
    public void requestList() {
        RepairRequest request = getRequest();
        List<RepairRequest> requestList = List.of(request);

        when(requestRepository.findAll()).thenReturn(requestList);
        List<RepairRequest> repairRequests = requestService.requestList();

        assertEquals(requestList, repairRequests);
    }

    @Test
    public void save() {
        String email = "testuser@mail.com";
        User user = getUser();
        RepairRequest request = getRequest();
        CustomUserDetails userDetails = getUserDetails();

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(requestRepository.save(any(RepairRequest.class))).thenReturn(request);

        requestService.save(request, userDetails);

        verify(userRepository, times(1)).findByEmail(email);
        verify(requestRepository, times(1)).save(request);
    }

    @Test
    public void save_AndReturnMoney() {
        RepairRequest request = getRequest();
        request.setCost(100L);
        CustomUserDetails userDetails = getUserDetails();

        requestService.save(request, userDetails);

        verify(userService, times(1)).updateBalance(userDetails, 100L);
    }

    @Test
    public void testSave() {
        RepairRequest request = getRequest();

        when(requestRepository.save(any(RepairRequest.class))).thenReturn(request);

        requestService.save(request);

        verify(requestRepository, times(1)).save(request);
    }

    @Test
    public void getById() {
        long id = 1L;
        RepairRequest request = getRequest();

        when(requestRepository.findById(id)).thenReturn(Optional.of(request));

        RepairRequest requestById = requestService.getById(id);

        assertEquals(request, requestById);
    }

    @Test
    public void deleteById() {
        long id = 1L;
        CustomUserDetails userDetails = getUserDetails();
        RepairRequest request = getRequest();

        when(requestRepository.findById(id)).thenReturn(Optional.of(request));
        doNothing().when(requestRepository).deleteById(id);

        requestService.deleteById(id, userDetails);

        verify(requestRepository, times(1)).deleteById(id);
    }

    @Test
    public void changePaymentStatus() {
        long id = 1L;
        RepairRequest request = getRequest();

        when(requestRepository.findById(id)).thenReturn(Optional.of(request));
        when(requestRepository.save(any(RepairRequest.class))).thenReturn(request);

        requestService.changePaymentStatus(id, RepairRequestPaymentStatus.PAID);

        verify(requestRepository, times(1)).findById(id);
        verify(requestRepository, times(1)).save(request);

        RepairRequestPaymentStatus paymentStatus = requestRepository.save(request).getPaymentStatus();
        assertEquals(RepairRequestPaymentStatus.PAID, paymentStatus);
    }

    @Test
    public void changeCompletionStatus() {
        long id = 1L;
        RepairRequest request = getRequest();

        when(requestRepository.findById(id)).thenReturn(Optional.of(request));
        when(requestRepository.save(any(RepairRequest.class))).thenReturn(request);

        requestService.changeCompletionStatus(id, RepairRequestCompletionStatus.COMPLETED);

        verify(requestRepository, times(1)).findById(id);
        verify(requestRepository, times(1)).save(request);

        RepairRequestCompletionStatus completionStatus = requestRepository.save(request).getCompletionStatus();
        assertEquals(RepairRequestCompletionStatus.COMPLETED, completionStatus);
    }

    @Test
    public void findPaginated() {
        int pageNumber = 1;
        int pageSize = 5;
        long userId = 1L;
        RepairRequest clientRequest = RepairRequest.builder().description("client request").build();
        RepairRequest repairerRequest = RepairRequest.builder().description("repairer request").build();

        Page<RepairRequest> clientPage = new PageImpl<>(List.of(clientRequest));
        Page<RepairRequest> repairerPage = new PageImpl<>(List.of(repairerRequest));
        Page<RepairRequest> managerPage = new PageImpl<>(List.of(clientRequest, repairerRequest));

        when(requestRepository.searchByClientId(any(), any(PageRequest.class))).thenReturn(clientPage);
        when(requestRepository.searchByRepairerId(any(), any(PageRequest.class))).thenReturn(repairerPage);
        when(requestRepository.findAll(any(PageRequest.class))).thenReturn(managerPage);

        Page<RepairRequest> page1 = requestService.findPaginated(pageNumber, pageSize, UserRole.ROLE_CLIENT, userId);
        String description1 = page1.getContent().get(0).getDescription();
        assertEquals("client request", description1);
        assertEquals(1, page1.getTotalElements());

        Page<RepairRequest> page2 = requestService.findPaginated(pageNumber, pageSize, UserRole.ROLE_REPAIRER, userId);
        String description2 = page2.getContent().get(0).getDescription();
        assertEquals("repairer request", description2);
        assertEquals(1, page2.getTotalElements());

        Page<RepairRequest> page3 = requestService.findPaginated(pageNumber, pageSize, UserRole.ROLE_MANAGER, userId);
        assertEquals(2, page3.getTotalElements());
    }

    @Test
    public void updateCost() {
        long id = 1L;
        long costUpdate = 100;

        RepairRequest request = getRequest();
        request.setClient(getUser());

        assertNotEquals(request.getCost(), costUpdate);

        when(requestRepository.findById(any())).thenReturn(Optional.of(request));
        when(requestRepository.save(any(RepairRequest.class))).thenReturn(request);

        requestService.updateCost(id, costUpdate);
        RepairRequest saved = requestRepository.save(request);

        assertEquals(saved, request);
        assertEquals(costUpdate, saved.getCost());
    }

    @Test
    public void updateCost_BalanceChargeBack() {
        long id = 1L;
        long costUpdate = 100L;

        RepairRequest request = getRequest();
        request.setCost(150L);
        request.setClient(getUser());

        when(requestRepository.findById(any())).thenReturn(Optional.of(request));

        requestService.updateCost(id, costUpdate);
        verify(userService, times(1)).balanceChargeBack(request.getClientId(), 50L);
    }

    @Test
    public void updateRepairer() {
        User repairer = User.builder().email("repairer@mail.com").build();
        RepairRequest request = getRequest();

        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(repairer));
        when(requestRepository.findById(any())).thenReturn(Optional.ofNullable(request));
        when(requestRepository.save(any(RepairRequest.class))).thenReturn(request);

        requestService.updateRepairer(1L, "1 Test Name");

        RepairRequest saved = requestRepository.save(request);
        assertEquals(request, saved);
    }

    @Test
    public void repairerList() {
        List<String> repairerList = List.of("Repairer1", "Repairer2", "Repairer3");

        when(userRepository.repairerNameList()).thenReturn(repairerList);

        List<String> repairers = requestService.repairerList();

        assertEquals(repairerList, repairers);
    }

    @Test
    public void payForRequest() {
        long id = 1L;
        CustomUserDetails userDetails = getUserDetails();

        RepairRequest request = getRequest();
        request.setCost(50L);

        User user = getUser();
        user.setBalance(150L);

        when(userRepository.findByEmail(any())).thenReturn(user);
        when(requestRepository.findById(id)).thenReturn(Optional.of(request));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(requestRepository.save(any(RepairRequest.class))).thenReturn(request);

        requestService.payForRequest(id, userDetails);

        assertEquals(50L, request.getDepositedToPay());
        assertEquals(RepairRequestPaymentStatus.PAID, request.getPaymentStatus());
        assertEquals(100L, user.getBalance());
        assertEquals(100L, userDetails.getBalance());
    }

    private RepairRequest getRequest() {
        return new RepairRequest();
    }

    private User getUser() {
        return User.builder()
                .id(1L)
                .role(UserRole.ROLE_CLIENT)
                .email("testuser@mail.com")
                .firstName("Test")
                .lastName("User")
                .password("password")
                .balance(0L)
                .build();
    }

    private CustomUserDetails getUserDetails() {
        User user = getUser();
        return new CustomUserDetails(user);
    }
}
