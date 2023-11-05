package ru.pukhov.shop.repository;


import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.pukhov.shop.model.Role;
import ru.pukhov.shop.repository.impl.RoleRepositoryImpl;

import java.util.Optional;

@Testcontainers
@Tag("DockerRequired")
class RoleRepositoryImplTest {
    private static final String INIT_SQL = "sql/schema_for_test.sql";
    @Container
    public static PostgreSQLContainer container = (PostgreSQLContainer) new PostgreSQLContainer("postgres:latest")
            .withUsername("root")
            .withPassword("1706")
            .withInitScript(INIT_SQL);
    public static RoleRepository roleRepository;
    private static JdbcDatabaseDelegate jdbcDatabaseDelegate;

    @BeforeAll
    static void beforeAll() {
        container.start();
        roleRepository = RoleRepositoryImpl.getInstance();
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
        String expectedName = "new Name";
        Role role = new Role(null, expectedName);
        role = roleRepository.save(role);
        Optional<Role> resultRole = roleRepository.findById(role.getId());

        Assertions.assertTrue(resultRole.isPresent());
        Assertions.assertEquals(expectedName, resultRole.get().getName());
    }

    @Test
    void update() {
        String expectedName = "update Name";

        Role roleForUpdate = roleRepository.findById(3L).get();
        String oldRoleName = roleForUpdate.getName();

        roleForUpdate.setName(expectedName);
        roleRepository.update(roleForUpdate);

        Role role = roleRepository.findById(3L).get();

        Assertions.assertNotEquals(expectedName, oldRoleName);
        Assertions.assertEquals(expectedName, role.getName());
    }

    @DisplayName("Delete by ID")
    @Test
    void deleteById() {
        Boolean expectedValue = true;
        int expectedSize = roleRepository.findAll().size();

        Role tempRole = new Role(null, "Role for delete.");
        tempRole = roleRepository.save(tempRole);

        boolean resultDelete = roleRepository.deleteById(tempRole.getId());
        int roleListAfterSize = roleRepository.findAll().size();

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
        Optional<Role> role = roleRepository.findById(expectedId);
        Assertions.assertEquals(expectedValue, role.isPresent());
        if (role.isPresent()) {
            Assertions.assertEquals(expectedId, role.get().getId());
        }
    }

    @Test
    void findAll() {
        int expectedSize = 5;
        int resultSize = roleRepository.findAll().size();

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
        boolean isRoleExist = roleRepository.existById(roleId);

        Assertions.assertEquals(expectedValue, isRoleExist);
    }
}