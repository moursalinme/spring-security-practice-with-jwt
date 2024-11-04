package com.security.backend.services;

import java.text.ParseException;

import com.security.backend.dtos.UserDto;
import com.security.backend.dtos.Request.LoginRequest;

public interface AuthService {

    UserDto handleRegister(UserDto user) throws Exception;

    UserDto handleLogin(LoginRequest loginReq) throws ParseException;

    UserDto makeAdminWithEmail(String email) throws ParseException;

}
