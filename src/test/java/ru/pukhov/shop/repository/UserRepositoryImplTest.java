package ru.pukhov.shop.repository;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import ru.pukhov.shop.model.Role;
import ru.pukhov.shop.model.User;
import ru.pukhov.shop.repository.impl.UserRepositoryImpl;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

class UserRepositoryImplTest {
    private static final String INIT_SQL = "sql/schema_for_test.sql";
    @Container
    public static PostgreSQLContainer container = (PostgreSQLContainer) new PostgreSQLContainer("postgres:latest")
            .withUsername("root")
            .withPassword("1706")
            .withInitScript(INIT_SQL);
    public static UserRepository userRepository;
    private static JdbcDatabaseDelegate jdbcDatabaseDelegate;

    @BeforeAll
    static void beforeAll() {
        container.start();
        userRepository = UserRepositoryImpl.getInstance();
        jdbcDatabaseDelegate = new JdbcDatabaseDelegate(container, "");
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @BeforeEach
    void setUp() {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, INIT_SQL);
    }

    @Test
    void save() {
        String expectedFirstname = "new Firstname";
        String expectedLastname = "new Lastname";

        User user = new User(
                null,
                expectedFirstname,
                expectedLastname,
                null,
                null,
                null);
        user = userRepository.save(user);
        Optional<User> resultUser = userRepository.findById(user.getId());

        Assertions.assertTrue(resultUser.isPresent());
        Assertions.assertEquals(expectedFirstname, resultUser.get().getFirstName());
        Assertions.assertEquals(expectedLastname, resultUser.get().getLastName());
    }

    @Test
    void testUpdateUser() throws SQLException {
        // Создание пользователя для обновления
        User user = new User(1L, "Firstname", "Lastname", new Role(1L, "Manager"), null, null);

        // Модификация имени и фамилии пользователя
        user.setFirstName("New name");
        user.setLastName("New lastname");

        // Обновление пользователя в репозитории
        userRepository.update(user);

        // Проверка, что пользователь успешно обновлен
        User updatedUser = userRepository.findById(1L).orElse(null);
        assertEquals("New name", updatedUser.getFirstName());
        assertEquals("New lastname", updatedUser.getLastName());

        // Проверка, что роль пользователя осталась неизменной
        assertEquals(1L, updatedUser.getRole().getId().longValue());
        assertEquals("Manager", updatedUser.getRole().getName());
    }

    @Test
    void deleteById() {
        Boolean expectedValue = true;
        int expectedSize = userRepository.findAll().size();

        User tempUser = new User(
                null,
                "User for delete Firstname.",
                "User for delete Lastname.",
                null,
                null,
                null
        );
        tempUser = userRepository.save(tempUser);

        boolean resultDelete = userRepository.deleteById(tempUser.getId());
        int roleListAfterSize = userRepository.findAll().size();

        Assertions.assertEquals(expectedValue, resultDelete);
        Assertions.assertEquals(expectedSize, roleListAfterSize);
    }

    @DisplayName("Find by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1; true",
            "4; true",
            "100; false"
    }, delimiter = ';')
    void findById(Long expectedId, Boolean expectedValue) {
        Optional<User> user = userRepository.findById(expectedId);
        Assertions.assertEquals(expectedValue, user.isPresent());
        user.ifPresent(value -> Assertions.assertEquals(expectedId, value.getId()));
    }

    @Test
    void findAll() {
        int expectedSize = 7;
        int resultSize = userRepository.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Exist by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1; true",
            "4; true",
            "100; false"
    }, delimiter = ';')
    void exitsById(Long roleId, Boolean expectedValue) {
        boolean isUserExist = userRepository.exitsById(roleId);

        Assertions.assertEquals(expectedValue, isUserExist);
    }
}