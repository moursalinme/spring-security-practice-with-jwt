package com.security.backend.services;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

import com.security.backend.dtos.UserDto;
import com.security.backend.models.Role;

public interface UserService {

    UserDto registerUser(UserDto user) throws ParseException;

    List<UserDto> getAllUsers();

    UserDto getUserByEmail(String email);

    boolean deleteUserByEmail(String email);

    UserDto updateUserRoles(String email, Set<Role> roles) throws ParseException;

}
