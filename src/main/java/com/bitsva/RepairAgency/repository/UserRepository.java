package com.bitsva.RepairAgency.repository;

import com.bitsva.RepairAgency.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query(nativeQuery = true, value =
            "SELECT CONCAT(last_name, \" \", first_name) AS full_name FROM users WHERE role = 'ROLE_REPAIRER'")
    List<String> repairerNameList();

    @Query(nativeQuery = true, value =
            "SELECT * FROM users WHERE last_name = :query1 AND first_name = :query2")
    User findRepairerByName(@Param("query1") String query1, @Param("query2") String query2);

    @Modifying
    @Query(nativeQuery = true, value =
            "UPDATE users SET balance = balance + :amountOfMoney where id = :id")
    void updateBalance(@Param("amountOfMoney") long amountOfMoney, @Param("id") long id);
}
