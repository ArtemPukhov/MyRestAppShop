package ru.pukhov.shop.service.impl;


import ru.pukhov.shop.exception.NotFoundException;
import ru.pukhov.shop.model.User;
import ru.pukhov.shop.repository.UserRepository;
import ru.pukhov.shop.repository.impl.UserRepositoryImpl;
import ru.pukhov.shop.service.UserService;
import ru.pukhov.shop.servlet.dto.UserIncomingDto;
import ru.pukhov.shop.servlet.dto.UserOutGoingDto;
import ru.pukhov.shop.servlet.dto.UserUpdateDto;
import ru.pukhov.shop.servlet.mapper.UserDtoMapper;
import ru.pukhov.shop.servlet.mapper.impl.UserDtoMapperImpl;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository = UserRepositoryImpl.getInstance();
    private static final UserDtoMapper userDtoMapper = UserDtoMapperImpl.getInstance();
    private static UserService instance;


    private UserServiceImpl() {
    }

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl();
        }
        return instance;
    }

    private void checkExistUser(Long userId) throws NotFoundException {
        if (!userRepository.existById(userId)) {
            throw new NotFoundException("User not found.");
        }
    }

    @Override
    public UserOutGoingDto save(UserIncomingDto userDto) {
        User user = userRepository.save(userDtoMapper.map(userDto));
        return userDtoMapper.map(userRepository.findById(user.getId()).orElse(user));
    }

    @Override
    public void update(UserUpdateDto userDto) throws NotFoundException {
        if (userDto == null || userDto.getId() == null) {
            throw new IllegalArgumentException();
        }
        checkExistUser(userDto.getId());
        userRepository.update(userDtoMapper.map(userDto));
    }

    @Override
    public UserOutGoingDto findById(Long userId) throws NotFoundException {
        checkExistUser(userId);
        User user = userRepository.findById(userId).orElseThrow();
        return userDtoMapper.map(user);
    }

    @Override
    public List<UserOutGoingDto> findAll() {
        List<User> all = userRepository.findAll();
        return userDtoMapper.map(all);
    }

    @Override
    public void delete(Long userId) throws NotFoundException {
        checkExistUser(userId);
        userRepository.deleteById(userId);
    }
}
