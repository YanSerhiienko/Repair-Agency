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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RepairRequestServiceImpl implements RepairRequestService {
    private final RepairRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public List<RepairRequest> requestList() {
        return requestRepository.findAll();
    }

    @Transactional
    @Override
    public void save(RepairRequest request, CustomUserDetails loggedUser) {
        User user = userRepository.findByEmail(loggedUser.getUsername());
        request.setClient(user);

        if (request.getCost() > 0) {
            userService.updateBalance(loggedUser, request.getCost());
            request.setCost(0L);
            request.setPaymentStatus(RepairRequestPaymentStatus.AWAITING_PAYMENT);
        }

        request.setCreationDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        requestRepository.save(request);
    }

    @Override
    public void save(RepairRequest request) {
        requestRepository.save(request);
    }

    @Override
    public RepairRequest getById(long id) {
        return requestRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(long id) {
        requestRepository.deleteById(id);
    }

    @Override
    public void changePaymentStatus(long id, RepairRequestPaymentStatus paymentStatus) {
        RepairRequest request = requestRepository.findById(id).orElse(null);
        request.setPaymentStatus(paymentStatus);
        requestRepository.save(request);
    }

    @Override
    public void changeCompletionStatus(long id, RepairRequestCompletionStatus completionStatus) {
        RepairRequest request = requestRepository.findById(id).orElse(null);
        request.setCompletionStatus(completionStatus);
        requestRepository.save(request);
    }

    @Override
    public Page<RepairRequest> findPaginated(int page, int size, UserRole userRole, long userId) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        return switch (userRole) {
            case ROLE_CLIENT -> requestRepository.searchByClientId(userId, pageable);
            case ROLE_REPAIRER -> requestRepository.searchByRepairerId(userId, pageable);
            default -> requestRepository.findAll(pageable);
        };
    }

    @Transactional
    @Override
    public void updateCost(long id, long costUpdate) {
        RepairRequest request = requestRepository.findById(id).orElse(null);
        long moneyToChargeBack = 0;
        if (request.getCost() != null && request.getCost() > costUpdate) {
            moneyToChargeBack = request.getCost() - costUpdate;
            userService.balanceChargeBack(request.getClientId(), moneyToChargeBack);
        }
        long newDepositedToPay = request.getDepositedToPay() - moneyToChargeBack;
        request.setDepositedToPay(newDepositedToPay);
        request.setCost(costUpdate);
        requestRepository.save(request);
    }

    @Override
    public void updateRepairer(long requestId, String repairerName) {
        String[] splitName = repairerName.split(" ");
        Long repairerId = Long.valueOf(splitName[0]);
        User repairer = userRepository.findById(repairerId).orElse(null);
        RepairRequest request = requestRepository.findById(requestId).get();
        request.setRepairer(repairer);
        requestRepository.save(request);
    }

    @Override
    public List<String> repairerList() {
        return userRepository.repairerNameList();
    }

    @Transactional
    @Override
    public void payForRequest(long requestId, CustomUserDetails loggedUser) {
        User client = userRepository.findByEmail(loggedUser.getUsername());
        RepairRequest request = requestRepository.findById(requestId).orElse(null);

        Long balanceBeforePayment = client.getBalance();
        Long depositedToPay = request.getDepositedToPay();
        Long cost = request.getCost();
        long balanceAfterPayment = balanceBeforePayment - cost + depositedToPay;

        client.setBalance(balanceAfterPayment);
        request.setDepositedToPay(cost);

        request.setPaymentStatus(RepairRequestPaymentStatus.PAID);

        userRepository.save(client);
        requestRepository.save(request);

        loggedUser.setBalance(balanceAfterPayment);
    }
}
