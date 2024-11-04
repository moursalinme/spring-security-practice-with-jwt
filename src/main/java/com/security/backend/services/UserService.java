package com.security.backend.services;

import java.text.ParseException;
import java.util.List;

import com.security.backend.dtos.UserDto;
import com.security.backend.models.UserModel;

public interface UserService {

    UserDto registerUser(UserDto user) throws ParseException;

    List<UserDto> getAllUsers();

    UserModel getUserByEmail(String email);

}
