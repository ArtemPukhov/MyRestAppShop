package ru.pukhov.shop.service;


import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.pukhov.shop.exception.NotFoundException;
import ru.pukhov.shop.model.Role;
import ru.pukhov.shop.model.User;
import ru.pukhov.shop.repository.UserRepository;
import ru.pukhov.shop.repository.impl.UserRepositoryImpl;
import ru.pukhov.shop.service.impl.UserServiceImpl;
import ru.pukhov.shop.servlet.dto.RoleUpdateDto;
import ru.pukhov.shop.servlet.dto.UserIncomingDto;
import ru.pukhov.shop.servlet.dto.UserOutGoingDto;
import ru.pukhov.shop.servlet.dto.UserUpdateDto;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

class UserServiceImplTest {
    private static UserService userService;
    private static UserRepository mockUserRepository;
    private static Role role;
    private static UserRepositoryImpl oldInstance;

    private static void setMock(UserRepository mock) {
        try {
            Field instance = UserRepositoryImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldInstance = (UserRepositoryImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        role = new Role(1L, "role#1");
        mockUserRepository = Mockito.mock(UserRepository.class);
        setMock(mockUserRepository);
        userService = UserServiceImpl.getInstance();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = UserRepositoryImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldInstance);
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(mockUserRepository);
    }

    @Test
    void save() {
        Long expectedId = 1L;

        UserIncomingDto dto = new UserIncomingDto("f1 name", "l1 name", role);
        User user = new User(expectedId, "f1 name", "l1 name", role, List.of(), List.of());

        Mockito.doReturn(user).when(mockUserRepository).save(Mockito.any(User.class));

        UserOutGoingDto result = userService.save(dto);

        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void update() throws NotFoundException {
        Long expectedId = 1L;

        UserUpdateDto dto = new UserUpdateDto(expectedId, "f1 name", "l1 name",
                new RoleUpdateDto(1L, "role#1"), List.of(), List.of());

        Mockito.doReturn(true).when(mockUserRepository).existById(Mockito.any());

        userService.update(dto);

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(mockUserRepository).update(argumentCaptor.capture());

        User result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void updateNotFound() {
        UserUpdateDto dto = new UserUpdateDto(1L, "f1 name", "l1 name", null, null, null);

        Mockito.doReturn(false).when(mockUserRepository).existById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> {
                    userService.update(dto);
                }, "Not found."
        );
        Assertions.assertEquals("User not found.", exception.getMessage());
    }

    @Test
    void findById() throws NotFoundException {
        Long expectedId = 1L;

        Optional<User> user = Optional.of(new User(expectedId, "f1 name", "l1 name", role, List.of(), List.of()));

        Mockito.doReturn(true).when(mockUserRepository).existById(Mockito.any());
        Mockito.doReturn(user).when(mockUserRepository).findById(Mockito.anyLong());

        UserOutGoingDto dto = userService.findById(expectedId);

        Assertions.assertEquals(expectedId, dto.getId());
    }

    @Test
    void findByIdNotFound() {
        Optional<User> user = Optional.empty();

        Mockito.doReturn(false).when(mockUserRepository).existById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> {
                    userService.findById(1L);
                }, "Not found."
        );
        Assertions.assertEquals("User not found.", exception.getMessage());
    }

    @Test
    void findAll() {
        userService.findAll();
        Mockito.verify(mockUserRepository).findAll();
    }

    @Test
    void delete() throws NotFoundException {
        Long expectedId = 100L;

        Mockito.doReturn(true).when(mockUserRepository).existById(Mockito.any());
        userService.delete(expectedId);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(mockUserRepository).deleteById(argumentCaptor.capture());

        Long result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result);
    }
}