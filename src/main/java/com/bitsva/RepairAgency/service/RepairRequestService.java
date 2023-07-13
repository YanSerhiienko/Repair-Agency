package com.bitsva.RepairAgency.service;

import com.bitsva.RepairAgency.entity.RepairRequest;
import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.feature.RepairRequestCompletionStatus;
import com.bitsva.RepairAgency.feature.RepairRequestPaymentStatus;
import com.bitsva.RepairAgency.repository.RepairRequestRepository;
import com.bitsva.RepairAgency.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RepairRequestService {
    private final RepairRequestRepository requestRepository;
    private final UserRepository userRepository;

    public List<RepairRequest> requestList() {
        return requestRepository.findAll();
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

    public Page<RepairRequest> findPaginated(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        return requestRepository.findAll(pageable);
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
