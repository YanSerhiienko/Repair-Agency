package com.bitsva.RepairAgency.service;

import com.bitsva.RepairAgency.config.CustomUserDetails;
import com.bitsva.RepairAgency.entity.RepairRequest;
import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.feature.RepairRequestCompletionStatus;
import com.bitsva.RepairAgency.feature.RepairRequestPaymentStatus;
import com.bitsva.RepairAgency.feature.UserRole;
import com.bitsva.RepairAgency.repository.RepairRequestRepository;
import com.bitsva.RepairAgency.repository.UserRepository;
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
public class RepairRequestService {
    private final RepairRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public List<RepairRequest> requestList() {
        return requestRepository.findAll();
    }
    //TODO clean this up
    /*public Page<RepairRequest> requestListByUserId(String id) {
        return requestRepository.searchByUserId(id);
    }*/

    public void save(RepairRequest request, CustomUserDetails loggedUser) {
        User user = userRepository.findByEmail(loggedUser.getUsername());
        //request.getUsers().add(user);
        request.setClient(user);

        if (request.getCost() > 0) {
            userService.updateBalance(loggedUser, request.getCost());
            request.setCost(0L);
            request.setPaymentStatus(RepairRequestPaymentStatus.AWAITING_PAYMENT);
        }

        request.setCreationDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        requestRepository.save(request);
    }

    public void save(RepairRequest request) {
        requestRepository.save(request);
    }

    public RepairRequest getById(long id) {
        return requestRepository.findById(id).orElse(null);
    }

    public void deleteById(long id) {
        requestRepository.deleteById(id);
    }

    public void changePaymentStatus(long id, RepairRequestPaymentStatus paymentStatus) {
        RepairRequest request = getById(id);
        request.setPaymentStatus(paymentStatus);
        save(request);
    }

    public void changeCompletionStatus(long id, RepairRequestCompletionStatus completionStatus) {
        RepairRequest request = getById(id);
        request.setCompletionStatus(completionStatus);
        save(request);
    }

    public Page<RepairRequest> findPaginated(int page, int size, UserRole userRole, long userId) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        /*if (userRole.equals(UserRole.ROLE_MANAGER)) {
            return requestRepository.findAll(pageable);
        } else {
            return requestRepository.searchByUserId(String.valueOf(userId), pageable);
        }*/
        if (userRole.equals(UserRole.ROLE_MANAGER)) {
            return requestRepository.findAll(pageable);
        } else if (userRole.equals(UserRole.ROLE_CLIENT)){
            return requestRepository.searchByClientId(String.valueOf(userId), pageable);
        } else {
            return requestRepository.searchByRepairerId(String.valueOf(userId), pageable);
        }
    }

    @Transactional
    public void updateCost(long id, long costUpdate) {
        RepairRequest request = getById(id);
        long moneyToChargeBack = 0;
        if (request.getCost() != null && request.getCost() > costUpdate) {
            moneyToChargeBack = request.getCost() - costUpdate;
            userService.balanceChargeBack(request.getClientId(), moneyToChargeBack);
        }
        long newDepositedToPay = request.getDepositedToPay() - moneyToChargeBack;
        request.setDepositedToPay(newDepositedToPay);
        request.setCost(costUpdate);
        save(request);
    }

    public void updateRepairer(long id, String repairerName) {
        String[] splitName = repairerName.split(" ");
        //User repairer = userRepository.findRepairerByName(splitName[0], splitName[1]);
        User repairer = userRepository.findById(Long.valueOf(splitName[0])).orElse(null);
        RepairRequest request = getById(id);
        request.setRepairer(repairer);
        save(request);
    }

    public List<String> repairerList() {
        return userRepository.repairerNameList();
    }

    public void payForRequest(long requestId, CustomUserDetails loggedUser) {
        User client = userRepository.findByEmail(loggedUser.getUsername());
        RepairRequest request = getById(requestId);

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

    /*public void setRepairer(long requestId, long repairerId) {
        RepairRequest request = getById(requestId);
        User repairer = userRepository.findById(repairerId).orElse(null);
        request.setRepairer(repairer);
        requestRepository.save(request);
    }*/


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*public List<RepairRequest> searchByQuery(String completionStatus, String paymentStatus) {
        List<RepairRequest> allRequests = requestRepository.findAll();
        List<RepairRequest> filterByCompletionStatus = filterByCompletionStatus(completionStatus, allRequests);
        List<RepairRequest> filterByPaymentStatus = filterByPaymentStatus(paymentStatus, filterByCompletionStatus);
        System.out.println("repairRequests1 = " + filterByPaymentStatus);
        return filterByPaymentStatus;
    }

    private List<RepairRequest>  filterByCompletionStatus(String completionStatus, List<RepairRequest> requestStream) {
        if (completionStatus == null) {
            return requestStream;
        }
        String[] split = completionStatus.split(",");
        return switch (split.length) {
            case 1 ->
                    requestStream.stream().filter(it -> it.getCompletionStatus().toString().equals(completionStatus)).toList();
            case 2 ->
                    requestStream.stream().filter(it -> it.getCompletionStatus().toString().equals(split[0]) || it.getCompletionStatus().toString().equals(split[1])).toList();
            default -> requestStream;
        };
    }

    private List<RepairRequest> filterByPaymentStatus(String paymentStatus, List<RepairRequest> requestStream) {
        if (paymentStatus == null) {
            return requestStream;
        }
        String[] split = paymentStatus.split(",");
        return switch (split.length) {
            case 1 ->
                    requestStream.stream().filter(it -> it.getPaymentStatus().toString().equals(paymentStatus)).toList();
            case 2 ->
                    requestStream.stream().filter(it -> it.getPaymentStatus().toString().equals(split[0]) || it.getPaymentStatus().toString().equals(split[1])).toList();
            default -> requestStream;
        };
    }*/

    /*private List<RepairRequest> filterByAttachmentToRepairer(String attachmentToRepairer) {

    }

    private List<RepairRequest> filterByAttachmentToManager(String attachmentToManager) {

    }*/

    /*private List<RepairRequest> filterByCost(String cost) {

    }

    private List<RepairRequest> filterByCreationDate(String creationDate) {

    }*/
}
