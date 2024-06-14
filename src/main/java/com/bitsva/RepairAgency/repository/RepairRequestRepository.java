package com.bitsva.RepairAgency.repository;

import com.bitsva.RepairAgency.entity.RepairRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM requests WHERE client_id = :id")
    Page<RepairRequest> searchByClientId(@Param("id") Long id, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM requests WHERE repairer_id = :id")
    Page<RepairRequest> searchByRepairerId(@Param("id") Long id, Pageable pageable);
}
