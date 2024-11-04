package com.security.backend.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.security.backend.models.Role;
import com.security.backend.repositories.RoleRepository;
import com.security.backend.services.RoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name);

    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role addNewRole(Role role) {
        return roleRepository.save(role);
    }

}
