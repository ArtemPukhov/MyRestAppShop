package ru.pukhov.shop.servlet.mapper;


import org.junit.jupiter.api.*;
import ru.pukhov.shop.model.Role;
import ru.pukhov.shop.servlet.dto.RoleIncomingDto;
import ru.pukhov.shop.servlet.dto.RoleOutGoingDto;
import ru.pukhov.shop.servlet.dto.RoleUpdateDto;
import ru.pukhov.shop.servlet.mapper.impl.RoleDtoMapperImpl;

import java.util.List;

class RoleDtoMapperImplTest {
    private static Role role;
    private static RoleIncomingDto roleIncomingDto;
    private static RoleUpdateDto roleUpdateDto;
    private static RoleOutGoingDto roleOutGoingDto;
    private RoleDtoMapper roleDtoMapper;

    @BeforeAll
    static void beforeAll() {
        role = new Role(
                10L,
                "Role for Test"
        );

        roleIncomingDto = new RoleIncomingDto(
                "Incoming dto"
        );

        roleUpdateDto = new RoleUpdateDto(
                100L,
                "Update dto"
        );
    }

    @BeforeEach
    void setUp() {
        roleDtoMapper = RoleDtoMapperImpl.getInstance();
    }

    @DisplayName("Role map(RoleIncomingDto")
    @Test
    void mapIncoming() {
        Role resultRole = roleDtoMapper.map(roleIncomingDto);

        Assertions.assertNull(resultRole.getId());
        Assertions.assertEquals(roleIncomingDto.getName(), resultRole.getName());
    }

    @DisplayName("Role map(RoleUpdateDto")
    @Test
    void testMapUpdate() {
        Role resultRole = roleDtoMapper.map(roleUpdateDto);

        Assertions.assertEquals(roleUpdateDto.getId(), resultRole.getId());
        Assertions.assertEquals(roleUpdateDto.getName(), resultRole.getName());
    }

    @DisplayName("RoleOutGoingDto map(Role")
    @Test
    void testMapOutgoing() {
        RoleOutGoingDto resultRole = roleDtoMapper.map(role);

        Assertions.assertEquals(role.getId(), resultRole.getId());
        Assertions.assertEquals(role.getName(), resultRole.getName());
    }


    @DisplayName("List<RoleOutGoingDto> map(List<Role> roleList")
    @Test
    void testMapList() {
        List<RoleOutGoingDto> resultList = roleDtoMapper.map(
                List.of(
                        role,
                        role,
                        role
                )
        );

        Assertions.assertEquals(3, resultList.size());
    }
}