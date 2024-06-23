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
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldFindUserByEmail() {
        User user = User.builder()
                .firstName("Saul")
                .lastName("Goodman")
                .email("goodman@mail.com")
                .build();

        User savedUser = userRepository.save(user);

        User userByEmail = userRepository.findByEmail("goodman@mail.com");

        Assertions.assertThat(userByEmail).isEqualTo(savedUser);
    }

    @Test
    public void save_ReturnSavedUser() {
        User user = User.builder()
                .firstName("Saul")
                .lastName("Goodman")
                .email("goodman@mail.com")
                .build();

        User savedUser = userRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void getAll_ReturnMoreThenOneUser() {
        User user1 = User.builder()
                .firstName("Saul")
                .lastName("Goodman")
                .email("goodman@mail.com")
                .build();

        User user2 = User.builder()
                .firstName("Kim")
                .lastName("Wexler")
                .email("wexler@mail.com")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> userList = userRepository.findAll();

        Assertions.assertThat(userList).isNotNull();
        Assertions.assertThat(userList.size()).isEqualTo(2);
    }

    @Test
    public void findById_ReturnUserNotNull() {
        User user = User.builder()
                .firstName("Saul")
                .lastName("Goodman")
                .email("goodman@mail.com")
                .build();

        userRepository.save(user);

        User userById = userRepository.findById(user.getId()).get();

        Assertions.assertThat(userById).isNotNull();
    }

    @Test
    public void update_ReturnUserNotNull() {
        User user = User.builder()
                .firstName("Saul")
                .lastName("Goodman")
                .email("goodman@mail.com")
                .build();

        userRepository.save(user);

        User savedUser = userRepository.findById(user.getId()).get();
        savedUser.setFirstName("Jimmy");
        savedUser.setLastName("McGill");

        User updatedUser = userRepository.save(savedUser);

        Assertions.assertThat(updatedUser.getFirstName()).isNotNull();
        Assertions.assertThat(updatedUser.getLastName()).isNotNull();
        Assertions.assertThat(updatedUser.getFirstName()).isEqualTo("Jimmy");
        Assertions.assertThat(updatedUser.getLastName()).isEqualTo("McGill");
    }

    @Test
    public void delete_ReturnUserIsEmpty() {
        User user = User.builder()
                .firstName("Saul")
                .lastName("Goodman")
                .email("goodman@mail.com")
                .build();

        userRepository.save(user);

        userRepository.deleteById(user.getId());
        Optional<User> userById = userRepository.findById(user.getId());

        Assertions.assertThat(userById).isEmpty();
    }
}
