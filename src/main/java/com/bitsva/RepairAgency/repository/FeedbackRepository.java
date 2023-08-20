package com.bitsva.RepairAgency.repository;

import com.bitsva.RepairAgency.entity.Feedback;
import com.bitsva.RepairAgency.entity.RepairRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    @Query(nativeQuery = true, value = "SELECT AVG(rating) FROM feedback WHERE repairer_id = :repairerId")
    int averageRepairerRating(@Param("repairerId") long repairerId);

    @Query(nativeQuery = true, value = "SELECT * FROM feedback WHERE request_id = :requestId")
    Feedback findByRequestId(@Param("requestId") long requestId);
}
