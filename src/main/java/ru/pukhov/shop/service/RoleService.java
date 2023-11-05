package ru.pukhov.shop.service;


import ru.pukhov.shop.exception.NotFoundException;
import ru.pukhov.shop.servlet.dto.RoleIncomingDto;
import ru.pukhov.shop.servlet.dto.RoleOutGoingDto;
import ru.pukhov.shop.servlet.dto.RoleUpdateDto;

import java.util.List;

public interface RoleService {
    RoleOutGoingDto save(RoleIncomingDto role);

    void update(RoleUpdateDto role) throws NotFoundException;

    RoleOutGoingDto findById(Long roleId) throws NotFoundException;

    List<RoleOutGoingDto> findAll();

    boolean delete(Long roleId) throws NotFoundException;
}
