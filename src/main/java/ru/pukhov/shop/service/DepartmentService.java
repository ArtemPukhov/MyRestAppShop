package ru.pukhov.shop.service;


import ru.pukhov.shop.exception.NotFoundException;
import ru.pukhov.shop.servlet.dto.DepartmentIncomingDto;
import ru.pukhov.shop.servlet.dto.DepartmentOutGoingDto;
import ru.pukhov.shop.servlet.dto.DepartmentUpdateDto;

import java.util.List;

public interface DepartmentService {
    DepartmentOutGoingDto save(DepartmentIncomingDto department);

    void update(DepartmentUpdateDto department) throws NotFoundException;

    DepartmentOutGoingDto findById(Long departmentId) throws NotFoundException;

    List<DepartmentOutGoingDto> findAll();

    void delete(Long departmentId) throws NotFoundException;

    void deleteUserFromDepartment(Long departmentId, Long userId) throws NotFoundException;

    void addUserToDepartment(Long departmentId, Long userId) throws NotFoundException;
}
