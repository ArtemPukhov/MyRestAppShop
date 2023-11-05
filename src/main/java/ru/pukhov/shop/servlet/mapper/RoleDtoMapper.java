package ru.pukhov.shop.servlet.mapper;


import ru.pukhov.shop.model.Role;
import ru.pukhov.shop.servlet.dto.RoleIncomingDto;
import ru.pukhov.shop.servlet.dto.RoleOutGoingDto;
import ru.pukhov.shop.servlet.dto.RoleUpdateDto;

import java.util.List;

public interface RoleDtoMapper {
    Role map(RoleIncomingDto roleIncomingDto);

    Role map(RoleUpdateDto roleUpdateDto);

    RoleOutGoingDto map(Role role);

    List<RoleOutGoingDto> map(List<Role> roleList);
}
