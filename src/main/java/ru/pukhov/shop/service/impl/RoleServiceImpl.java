package ru.pukhov.shop.service.impl;


import ru.pukhov.shop.exception.NotFoundException;
import ru.pukhov.shop.model.Role;
import ru.pukhov.shop.repository.RoleRepository;
import ru.pukhov.shop.repository.impl.RoleRepositoryImpl;
import ru.pukhov.shop.service.RoleService;
import ru.pukhov.shop.servlet.dto.RoleIncomingDto;
import ru.pukhov.shop.servlet.dto.RoleOutGoingDto;
import ru.pukhov.shop.servlet.dto.RoleUpdateDto;
import ru.pukhov.shop.servlet.mapper.RoleDtoMapper;
import ru.pukhov.shop.servlet.mapper.impl.RoleDtoMapperImpl;

import java.util.List;

public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository = RoleRepositoryImpl.getInstance();
    private static RoleService instance;
    private final RoleDtoMapper roleDtoMapper = RoleDtoMapperImpl.getInstance();


    private RoleServiceImpl() {
    }

    public static synchronized RoleService getInstance() {
        if (instance == null) {
            instance = new RoleServiceImpl();
        }
        return instance;
    }

    @Override
    public RoleOutGoingDto save(RoleIncomingDto roleDto) {
        Role role = roleDtoMapper.map(roleDto);
        role = roleRepository.save(role);
        return roleDtoMapper.map(role);
    }

    @Override
    public void update(RoleUpdateDto roleUpdateDto) throws NotFoundException {
        checkRoleExist(roleUpdateDto.getId());
        Role role = roleDtoMapper.map(roleUpdateDto);
        roleRepository.update(role);
    }

    @Override
    public RoleOutGoingDto findById(Long roleId) throws NotFoundException {
        Role role = roleRepository.findById(roleId).orElseThrow(() ->
                new NotFoundException("Role not found."));
        return roleDtoMapper.map(role);
    }

    @Override
    public List<RoleOutGoingDto> findAll() {
        List<Role> roleList = roleRepository.findAll();
        return roleDtoMapper.map(roleList);
    }

    @Override
    public boolean delete(Long roleId) throws NotFoundException {
        checkRoleExist(roleId);
        return roleRepository.deleteById(roleId);
    }

    private void checkRoleExist(Long roleId) throws NotFoundException {
        if (!roleRepository.exitsById(roleId)) {
            throw new NotFoundException("Role not found.");
        }
    }
}
