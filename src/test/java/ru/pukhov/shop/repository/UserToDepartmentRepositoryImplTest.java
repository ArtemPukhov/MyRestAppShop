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
import ru.pukhov.shop.model.UserToDepartment;
import ru.pukhov.shop.repository.impl.UserToDepartmentRepositoryImpl;

import java.util.Optional;

class UserToDepartmentRepositoryImplTest {
    private static final String INIT_SQL = "sql/schema_for_test.sql";
    public static UserToDepartmentRepository userToDepartmentRepository;
    @Container
    public static PostgreSQLContainer container = (PostgreSQLContainer) new PostgreSQLContainer("postgres:latest")
            .withUsername("root")
            .withPassword("1706")
            .withInitScript(INIT_SQL);
    private static JdbcDatabaseDelegate jdbcDatabaseDelegate;

    @BeforeAll
    static void beforeAll() {
        container.start();
        userToDepartmentRepository = UserToDepartmentRepositoryImpl.getInstance();
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
        Long expectedUserId = 1L;
        Long expectedDepartmentId = 3L;
        UserToDepartment link = new UserToDepartment(
                null,
                expectedUserId,
                expectedDepartmentId
        );
        link = userToDepartmentRepository.save(link);
        Optional<UserToDepartment> resultLink = userToDepartmentRepository.findById(link.getId());

        Assertions.assertTrue(resultLink.isPresent());
        Assertions.assertEquals(expectedUserId, resultLink.get().getUserId());
        Assertions.assertEquals(expectedDepartmentId, resultLink.get().getDepartmentId());
    }

    @Test
    void update() {
        Long expectedUserId = 1L;
        Long expectedDepartmentId = 4L;

        UserToDepartment link = userToDepartmentRepository.findById(2L).get();

        Long oldDepartmentId = link.getDepartmentId();
        Long oldUserId = link.getUserId();

        Assertions.assertNotEquals(expectedUserId, oldUserId);
        Assertions.assertNotEquals(expectedDepartmentId, oldDepartmentId);

        link.setUserId(expectedUserId);
        link.setDepartmentId(expectedDepartmentId);

        userToDepartmentRepository.update(link);

        UserToDepartment resultLink = userToDepartmentRepository.findById(2L).get();

        Assertions.assertEquals(link.getId(), resultLink.getId());
        Assertions.assertEquals(expectedUserId, resultLink.getUserId());
        Assertions.assertEquals(expectedDepartmentId, resultLink.getDepartmentId());
    }

    @Test
    void deleteById() {
        Boolean expectedValue = true;
        int expectedSize = userToDepartmentRepository.findAll().size();

        UserToDepartment link = new UserToDepartment(null, 1L, 2L);
        link = userToDepartmentRepository.save(link);

        int resultSizeBefore = userToDepartmentRepository.findAll().size();
        Assertions.assertNotEquals(expectedSize, resultSizeBefore);

        boolean resultDelete = userToDepartmentRepository.deleteById(link.getId());

        int resultSizeAfter = userToDepartmentRepository.findAll().size();

        Assertions.assertEquals(expectedValue, resultDelete);
        Assertions.assertEquals(expectedSize, resultSizeAfter);
    }

    @DisplayName("Delete by UserId.")
    @ParameterizedTest
    @CsvSource(value = {
            "2, false",
            "1000, false"
    })
    void deleteByUserId(Long expectedUserId, Boolean expectedValue) {
        int beforeSize = userToDepartmentRepository.findAllByUserId(expectedUserId).size();
        Boolean resultDelete = userToDepartmentRepository.deleteByUserId(expectedUserId);

        int afterDelete = userToDepartmentRepository.findAllByUserId(expectedUserId).size();

        Assertions.assertEquals(expectedValue, resultDelete);
        if (beforeSize != 0) {
            Assertions.assertNotEquals(beforeSize, afterDelete);
        }
    }

    @DisplayName("Delete by Department Id.")
    @ParameterizedTest
    @CsvSource(value = {
            "2, true",
            "1000, false"
    })
    void deleteByDepartmentId(Long expectedDepartmentId, Boolean expectedValue) {
        int beforeSize = userToDepartmentRepository.findAllByDepartmentId(expectedDepartmentId).size();
        Boolean resultDelete = userToDepartmentRepository.deleteByDepartmentId(expectedDepartmentId);

        int afterDelete = userToDepartmentRepository.findAllByDepartmentId(expectedDepartmentId).size();

        Assertions.assertEquals(expectedValue, resultDelete);
        if (beforeSize != 0) {
            Assertions.assertNotEquals(beforeSize, afterDelete);
        }
    }

    @DisplayName("Delete by Id.")
    @ParameterizedTest
    @CsvSource(value = {
            "1, true, 1, 1",
            "3, false, 3, 2",
            "1000, false, 0, 0"
    })
    void findById(Long expectedId, Boolean expectedValue, Long expectedUserId, Long expectedDepartmentId) {
        Optional<UserToDepartment> link = userToDepartmentRepository.findById(expectedId);

        Assertions.assertEquals(expectedValue, link.isPresent());
        if (link.isPresent()) {
            Assertions.assertEquals(expectedId, link.get().getId());
            Assertions.assertEquals(expectedUserId, link.get().getUserId());
            Assertions.assertEquals(expectedDepartmentId, link.get().getDepartmentId());
        }
    }

    @Test
    void findAll() {
        int expectedSize = 8;
        int resultSize = userToDepartmentRepository.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Exist by Id.")
    @ParameterizedTest
    @CsvSource(value = {
            "1, true",
            "3, true",
            "1000, false"
    })
    void exitsById(Long expectedId, Boolean expectedValue) {
        Boolean resultValue = userToDepartmentRepository.exitsById(expectedId);

        Assertions.assertEquals(expectedValue, resultValue);
    }

    @DisplayName("Find by user Id.")
    @ParameterizedTest
    @CsvSource(value = {
            "1, 1",
            "6, 2",
            "1000, 0"
    })
    void findAllByUserId(Long userId, int expectedSize) {
        int resultSize = userToDepartmentRepository.findAllByUserId(userId).size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Find by user Id.")
    @ParameterizedTest
    @CsvSource(value = {
            "3, 0",
            "6, 2",
            "1000, 0"
    })
    void findDepartmentsByUserId(Long userId, int expectedSize) {
        int resultSize = userToDepartmentRepository.findDepartmentsByUserId(userId).size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Find Department by user Id.")
    @ParameterizedTest
    @CsvSource(value = {
            "1, 3",
            "2, 3",
            "3, 1",
            "1000, 0"
    })
    void findAllByDepartmentId(Long departmentId, int expectedSize) {
        int resultSize = userToDepartmentRepository.findAllByDepartmentId(departmentId).size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Find Users by Department Id.")
    @ParameterizedTest
    @CsvSource(value = {
            "1, 3",
            "2, 3",
            "3, 1",
            "1000, 0"
    })
    void findUsersByDepartmentId(Long departmentId, int expectedSize) {
        int resultSize = userToDepartmentRepository.findUsersByDepartmentId(departmentId).size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Find Users by Department Id.")
    @ParameterizedTest
    @CsvSource(value = {
            "1, 1, true",
            "1, 4, true"
    })
    void findByUserIdAndDepartmentId(Long userId, Long departmentId, Boolean expectedValue) {
        Optional<UserToDepartment> link = userToDepartmentRepository.findByUserIdAndDepartmentId(userId, departmentId);

        Assertions.assertEquals(expectedValue, link.isPresent());
    }
}