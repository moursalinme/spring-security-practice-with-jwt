package com.security.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.backend.dtos.UserDto;
import com.security.backend.dtos.Response.ApiResponse;
import com.security.backend.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers(HttpServletRequest request) {
        return ApiResponse.success(request.getRequestURI(), "All users.", 200, userService.getAllUsers());
    }

}
