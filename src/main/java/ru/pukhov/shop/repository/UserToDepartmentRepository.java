package ru.pukhov.shop.repository;


import ru.pukhov.shop.model.Department;
import ru.pukhov.shop.model.User;
import ru.pukhov.shop.model.UserToDepartment;

import java.util.List;
import java.util.Optional;

public interface UserToDepartmentRepository extends Repository<UserToDepartment, Long> {
    boolean deleteByUserId(Long userId);

    boolean deleteByDepartmentId(Long departmentId);

    List<UserToDepartment> findAllByUserId(Long userId);

    List<Department> findDepartmentsByUserId(Long userId);

    List<UserToDepartment> findAllByDepartmentId(Long departmentId);

    List<User> findUsersByDepartmentId(Long departmentId);

    Optional<UserToDepartment> findByUserIdAndDepartmentId(Long userId, Long departmentId);
}
