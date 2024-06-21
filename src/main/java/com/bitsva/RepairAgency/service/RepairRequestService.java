package com.bitsva.RepairAgency.service;

import com.bitsva.RepairAgency.config.CustomUserDetails;
import com.bitsva.RepairAgency.entity.RepairRequest;
import com.bitsva.RepairAgency.feature.RepairRequestCompletionStatus;
import com.bitsva.RepairAgency.feature.RepairRequestPaymentStatus;
import com.bitsva.RepairAgency.feature.UserRole;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RepairRequestService {
    public List<RepairRequest> requestList();

    public void save(RepairRequest request, CustomUserDetails loggedUser);

    public void save(RepairRequest request);

    public RepairRequest getById(long id);

    public void deleteById(long id, CustomUserDetails loggedUser);

    public void changePaymentStatus(long id, RepairRequestPaymentStatus paymentStatus);

    public void changeCompletionStatus(long id, RepairRequestCompletionStatus completionStatus);

    public Page<RepairRequest> findPaginated(int page, int size, UserRole userRole, long userId);

    public void updateCost(long id, long costUpdate);

    public void updateRepairer(long id, String repairerName);

    public List<String> repairerList();

    public void payForRequest(long requestId, CustomUserDetails loggedUser);
}
