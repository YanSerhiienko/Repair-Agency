package com.bitsva.RepairAgency.repository;

import com.bitsva.RepairAgency.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_Save_ReturnSavedUser() {
        User user = User.builder()
                .firstName("Saul")
                .lastName("Goodman")
                .build();

        User savedUser = userRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void UserRepository_GetAll_ReturnMoreThenOneUser() {
        User user1 = User.builder()
                .firstName("Saul")
                .lastName("Goodman")
                .build();

        User user2 = User.builder()
                .firstName("Kim")
                .lastName("Wexler")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> userList = userRepository.findAll();

        Assertions.assertThat(userList).isNotNull();
        Assertions.assertThat(userList.size()).isEqualTo(2);
    }

    @Test
    public void UserRepository_FindById_ReturnUserNotNull() {
        User user = User.builder()
                .firstName("Saul")
                .lastName("Goodman")
                .build();

        userRepository.save(user);

        User userById = userRepository.findById(user.getId()).get();

        Assertions.assertThat(userById).isNotNull();
    }

    @Test
    public void UserRepository_UpdateUser_ReturnUserNotNull() {
        User user = User.builder()
                .firstName("Saul")
                .lastName("Goodman")
                .build();

        userRepository.save(user);

        User savedUser = userRepository.findById(user.getId()).get();
        savedUser.setFirstName("Jimmy");
        savedUser.setLastName("McGill");

        User updatedUser = userRepository.save(savedUser);

        Assertions.assertThat(updatedUser.getFirstName()).isNotNull();
        Assertions.assertThat(updatedUser.getLastName()).isNotNull();
    }

    @Test
    public void UserRepository_UserDelete_ReturnUserIsEmpty() {
        User user = User.builder()
                .firstName("Saul")
                .lastName("Goodman")
                .build();

        userRepository.save(user);

        userRepository.deleteById(user.getId());
        Optional<User> userById = userRepository.findById(user.getId());

        Assertions.assertThat(userById).isEmpty();
    }
}
