package com.bitsva.RepairAgency.repository;

import com.bitsva.RepairAgency.entity.User;
import com.bitsva.RepairAgency.feature.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

//@DataJpaTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Testcontainers
@SpringBootTest
public class UserRepositoryTest {
    @Container
    public static MySQLContainer container = new MySQLContainer()
            .withUsername("saul")
            .withPassword("1111")
            .withDatabaseName("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

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

//////////////////////////
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
