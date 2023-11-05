package ru.pukhov.shop.servlet.mapper;


import ru.pukhov.shop.model.Department;
import ru.pukhov.shop.servlet.dto.DepartmentIncomingDto;
import ru.pukhov.shop.servlet.dto.DepartmentOutGoingDto;
import ru.pukhov.shop.servlet.dto.DepartmentUpdateDto;

import java.util.List;

public interface DepartmentDtoMapper {
    Department map(DepartmentIncomingDto departmentIncomingDto);

    DepartmentOutGoingDto map(Department department);

    Department map(DepartmentUpdateDto departmentUpdateDto);

    List<DepartmentOutGoingDto> map(List<Department> departmentList);

    List<Department> mapUpdateList(List<DepartmentUpdateDto> departmentList);
}
