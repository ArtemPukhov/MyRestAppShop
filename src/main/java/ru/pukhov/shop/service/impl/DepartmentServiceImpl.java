package ru.pukhov.shop.service.impl;


import ru.pukhov.shop.exception.NotFoundException;
import ru.pukhov.shop.model.Department;
import ru.pukhov.shop.model.UserToDepartment;
import ru.pukhov.shop.repository.DepartmentRepository;
import ru.pukhov.shop.repository.UserRepository;
import ru.pukhov.shop.repository.UserToDepartmentRepository;
import ru.pukhov.shop.repository.impl.DepartmentRepositoryImpl;
import ru.pukhov.shop.repository.impl.UserRepositoryImpl;
import ru.pukhov.shop.repository.impl.UserToDepartmentRepositoryImpl;
import ru.pukhov.shop.service.DepartmentService;
import ru.pukhov.shop.servlet.dto.DepartmentIncomingDto;
import ru.pukhov.shop.servlet.dto.DepartmentOutGoingDto;
import ru.pukhov.shop.servlet.dto.DepartmentUpdateDto;
import ru.pukhov.shop.servlet.mapper.DepartmentDtoMapper;
import ru.pukhov.shop.servlet.mapper.impl.DepartmentDtoMapperImpl;

import java.util.List;

public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository = DepartmentRepositoryImpl.getInstance();
    private final UserRepository userRepository = UserRepositoryImpl.getInstance();
    private final UserToDepartmentRepository userToDepartmentRepository = UserToDepartmentRepositoryImpl.getInstance();
    private static final DepartmentDtoMapper departmentDtoMapper = DepartmentDtoMapperImpl.getInstance();
    private static DepartmentService instance;


    private DepartmentServiceImpl() {
    }

    public static synchronized DepartmentService getInstance() {
        if (instance == null) {
            instance = new DepartmentServiceImpl();
        }
        return instance;
    }

    private void checkExistDepartment(Long departmentId) throws NotFoundException {
        if (!departmentRepository.exitsById(departmentId)) {
            throw new NotFoundException("Department not found.");
        }
    }

    @Override
    public DepartmentOutGoingDto save(DepartmentIncomingDto departmentDto) {
        Department department = departmentDtoMapper.map(departmentDto);
        department = departmentRepository.save(department);
        return departmentDtoMapper.map(department);
    }

    @Override
    public void update(DepartmentUpdateDto departmentUpdateDto) throws NotFoundException {
        checkExistDepartment(departmentUpdateDto.getId());
        Department department = departmentDtoMapper.map(departmentUpdateDto);
        departmentRepository.update(department);
    }

    @Override
    public DepartmentOutGoingDto findById(Long departmentId) throws NotFoundException {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() ->
                new NotFoundException("Department not found."));
        return departmentDtoMapper.map(department);
    }

    @Override
    public List<DepartmentOutGoingDto> findAll() {
        List<Department> departmentList = departmentRepository.findAll();
        return departmentDtoMapper.map(departmentList);
    }

    @Override
    public void delete(Long departmentId) throws NotFoundException {
        checkExistDepartment(departmentId);
        departmentRepository.deleteById(departmentId);
    }

    @Override
    public void deleteUserFromDepartment(Long departmentId, Long userId) throws NotFoundException {
        checkExistDepartment(departmentId);
        if (userRepository.exitsById(userId)) {
            UserToDepartment linkUserDepartment = userToDepartmentRepository.findByUserIdAndDepartmentId(userId, departmentId)
                    .orElseThrow(() -> new NotFoundException("Link many to many Not found."));

            userToDepartmentRepository.deleteById(linkUserDepartment.getId());
        } else {
            throw new NotFoundException("User not found.");
        }

    }

    @Override
    public void addUserToDepartment(Long departmentId, Long userId) throws NotFoundException {
        checkExistDepartment(departmentId);
        if (userRepository.exitsById(userId)) {
            UserToDepartment linkUserDepartment = new UserToDepartment(
                    null,
                    userId,
                    departmentId
            );
            userToDepartmentRepository.save(linkUserDepartment);
        } else {
            throw new NotFoundException("User not found.");
        }

    }

}
