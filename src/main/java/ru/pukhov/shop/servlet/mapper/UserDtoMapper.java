package ru.pukhov.shop.servlet.mapper;


import ru.pukhov.shop.model.User;
import ru.pukhov.shop.servlet.dto.UserIncomingDto;
import ru.pukhov.shop.servlet.dto.UserOutGoingDto;
import ru.pukhov.shop.servlet.dto.UserUpdateDto;

import java.util.List;

public interface UserDtoMapper {
    User map(UserIncomingDto userIncomingDto);

    User map(UserUpdateDto userIncomingDto);

    UserOutGoingDto map(User user);

    List<UserOutGoingDto> map(List<User> user);

}
