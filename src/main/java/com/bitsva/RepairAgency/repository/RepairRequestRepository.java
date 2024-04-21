package com.bitsva.RepairAgency.repository;

import com.bitsva.RepairAgency.entity.RepairRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long> {

    @Query(nativeQuery = true, value =
            "SELECT email, full_name AS fullName, birthday, gender FROM \"user\"\n" +
                    "WHERE lower(email) like lower(:query) or lower(full_name) like lower(:query)")
    List<RepairRequest> searchByNativeSqlQuery(@Param("query") String query);

    @Query(nativeQuery = true, value = "SELECT * FROM request WHERE completion_status IN (:query)")
    List<RepairRequest> searchByQuery(@Param("query") String query);

    @Query(nativeQuery = true, value = "SELECT * FROM requests WHERE client_id = :query")
    Page<RepairRequest> searchByClientId(@Param("query") String query, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM requests WHERE repairer_id = :query")
    Page<RepairRequest> searchByRepairerId(@Param("query") String query, Pageable pageable);
}
