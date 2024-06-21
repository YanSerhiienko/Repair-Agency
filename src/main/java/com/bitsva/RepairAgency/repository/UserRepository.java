package com.bitsva.RepairAgency.repository;

import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.feature.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByRole(UserRole role);

    @Query(nativeQuery = true, value =
            "SELECT CONCAT(id, \" \", last_name, \" \", first_name) AS full_name FROM users WHERE role = 'ROLE_REPAIRER'")
    List<String> repairerNameList();

    @Modifying
    @Query(nativeQuery = true, value =
            "UPDATE users SET balance = balance + :amountOfMoney where id = :id")
    void updateBalance(@Param("amountOfMoney") long amountOfMoney, @Param("id") long id);

    @Modifying
    @Query(nativeQuery = true, value =
            "UPDATE users SET rating = :rating where id = :id")
    void updateRating(@Param("rating") float rating, @Param("id") long id);

    @Query(nativeQuery = true, value = "SELECT COUNT(1) FROM users WHERE email = :email")
    int checkIfEmailExists(@Param("email") String email);

    @Query(nativeQuery = true, value = "SELECT COUNT(1) FROM users WHERE email = :email AND id != :id")
    int checkIfEmailLinkedToAnotherUser(@Param("email") String email, @Param("id") Long id);

    @Query(nativeQuery = true, value = "SELECT COUNT(1) FROM users WHERE phone = :phone")
    int checkIfPhoneExists(@Param("phone") String phone);

    @Query(nativeQuery = true, value = "SELECT COUNT(1) FROM users WHERE phone = :phone AND id != :id")
    int checkIfPhoneLinkedToAnotherUser(@Param("phone") String phone, @Param("id") Long id);

    @Query(nativeQuery = true, value = "SELECT COUNT(1) FROM users WHERE role = 'ROLE_SUPER_ADMIN' AND id = :id")
    int checkUserAreNotSuperAdmin(@Param("id") Long id);
}
