package com.security.backend.services;

import java.util.List;

import com.security.backend.models.Role;

public interface RoleService {

    Role getRoleByName(String name);

    Role addNewRole(Role role);

    List<Role> getAllRoles();
}
