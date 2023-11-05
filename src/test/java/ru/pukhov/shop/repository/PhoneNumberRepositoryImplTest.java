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
import ru.pukhov.shop.model.PhoneNumber;
import ru.pukhov.shop.repository.impl.PhoneNumberRepositoryImpl;

import java.util.Optional;

class PhoneNumberRepositoryImplTest {
    private static final String INIT_SQL = "sql/schema_for_test.sql";
    public static PhoneNumberRepository phoneNumberRepository;
    @Container
    public static PostgreSQLContainer container = (PostgreSQLContainer) new PostgreSQLContainer("postgres:latest")
            .withUsername("root")
            .withPassword("1706")
            .withInitScript(INIT_SQL);
    private static JdbcDatabaseDelegate jdbcDatabaseDelegate;

    @BeforeAll
    static void beforeAll() {
        container.start();
        phoneNumberRepository = PhoneNumberRepositoryImpl.getInstance();
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
        String expectedNumber = "+3 (123) 123 4324";
        PhoneNumber phoneNumber = new PhoneNumber(
                null,
                expectedNumber,
                null
        );
        phoneNumber = phoneNumberRepository.save(phoneNumber);
        Optional<PhoneNumber> resultPhone = phoneNumberRepository.findById(phoneNumber.getId());

        Assertions.assertTrue(resultPhone.isPresent());
        Assertions.assertEquals(expectedNumber, resultPhone.get().getNumber());
    }

    @Test
    void update() {
        String expectedNumber = "+3 (321) 321 4324";

        PhoneNumber phoneNumberUpdate = phoneNumberRepository.findById(3L).get();
        String oldPhoneNumber = phoneNumberUpdate.getNumber();

        phoneNumberUpdate.setNumber(expectedNumber);
        phoneNumberRepository.update(phoneNumberUpdate);

        PhoneNumber number = phoneNumberRepository.findById(3L).get();

        Assertions.assertNotEquals(expectedNumber, oldPhoneNumber);
        Assertions.assertEquals(expectedNumber, number.getNumber());
    }

    @DisplayName("Delete by ID")
    @Test
    void deleteById() {
        Boolean expectedValue = true;
        int expectedSize = phoneNumberRepository.findAll().size();

        PhoneNumber tempNumber = new PhoneNumber(null, "+(temp) number", null);
        tempNumber = phoneNumberRepository.save(tempNumber);

        int resultSizeBefore = phoneNumberRepository.findAll().size();
        Assertions.assertNotEquals(expectedSize, resultSizeBefore);

        boolean resultDelete = phoneNumberRepository.deleteById(tempNumber.getId());
        int resultSizeListAfter = phoneNumberRepository.findAll().size();

        Assertions.assertEquals(expectedValue, resultDelete);
        Assertions.assertEquals(expectedSize, resultSizeListAfter);
    }

    @DisplayName("Delete by ID")
    @Test
    void deleteByUserId() {
        phoneNumberRepository.deleteByUserId(1L);
        int expectedSize = phoneNumberRepository.findAll().size() - phoneNumberRepository.findAllByUserId(1L).size();
        int resultSize = phoneNumberRepository.findAll().size();
        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Check exist by Phone number.")
    @ParameterizedTest
    @CsvSource(value = {
            "'+1(123)456 7891',false",
            "'not exits number', false"
    })
    void existsByNumber(String number, Boolean expectedValue) {
        boolean isExist = phoneNumberRepository.existsByNumber(number);

        Assertions.assertEquals(expectedValue, isExist);
    }

    @DisplayName("Find by Phone number.")
    @ParameterizedTest
    @CsvSource(value = {
            "'+1(123)123 5555',false",
            "'not exits number', false"
    })
    void findByNumber(String findNumber, Boolean expectedValue) {
        Optional<PhoneNumber> phoneNumber = phoneNumberRepository.findByNumber(findNumber);

        Assertions.assertEquals(expectedValue, phoneNumber.isPresent());
        if (phoneNumber.isPresent()) {
            Assertions.assertEquals(findNumber, phoneNumber.get().getNumber());
        }
    }

    @DisplayName("Find by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "4, true",
            "1000, false"
    })
    void findById(Long expectedId, Boolean expectedValue) {
        Optional<PhoneNumber> phoneNumber = phoneNumberRepository.findById(expectedId);

        Assertions.assertEquals(expectedValue, phoneNumber.isPresent());
        if (phoneNumber.isPresent()) {
            Assertions.assertEquals(expectedId, phoneNumber.get().getId());
        }
    }

    @Test
    void findAll() {
        int expectedSize = 9;
        int resultSize = phoneNumberRepository.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Exist by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "7, true",
            "500, false"
    })
    void exitsById(Long expectedId, Boolean expectedValue) {
        Boolean resultValue = phoneNumberRepository.exitsById(expectedId);

        Assertions.assertEquals(expectedValue, resultValue);
    }

    @DisplayName("Find by UserId")
    @ParameterizedTest
    @CsvSource(value = {
            "2, 2",
            "3, 1",
            "500, 0"
    })
    void findAllByUserId(Long userId, int expectedSize) {
        int resultSize = phoneNumberRepository.findAllByUserId(userId).size();

        Assertions.assertEquals(expectedSize, resultSize);
    }
}