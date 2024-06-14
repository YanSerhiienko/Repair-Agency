package com.bitsva.RepairAgency.repository;

import com.bitsva.RepairAgency.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    @Query(nativeQuery = true, value = "SELECT AVG(rating) FROM feedbacks WHERE repairer_id = :repairerId")
    float averageRepairerRating(@Param("repairerId") long repairerId);

    @Query(nativeQuery = true, value = "SELECT * FROM feedbacks WHERE request_id = :requestId")
    Feedback findByRequestId(@Param("requestId") long requestId);
}
