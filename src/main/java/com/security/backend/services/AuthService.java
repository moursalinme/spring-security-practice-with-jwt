package com.security.backend.services;

import com.security.backend.dtos.UserDto;
import com.security.backend.dtos.Request.LoginRequest;

public interface AuthService {

    UserDto handleRegister(UserDto user) throws Exception;

    UserDto handleLogin(LoginRequest loginReq);

}
