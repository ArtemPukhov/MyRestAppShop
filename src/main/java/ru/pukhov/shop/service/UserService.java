package ru.pukhov.shop.service;


import ru.pukhov.shop.exception.NotFoundException;
import ru.pukhov.shop.servlet.dto.UserIncomingDto;
import ru.pukhov.shop.servlet.dto.UserOutGoingDto;
import ru.pukhov.shop.servlet.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    UserOutGoingDto save(UserIncomingDto userDto);

    void update(UserUpdateDto userDto) throws NotFoundException;

    UserOutGoingDto findById(Long userId) throws NotFoundException;

    List<UserOutGoingDto> findAll();

    void delete(Long userId) throws NotFoundException;
}
