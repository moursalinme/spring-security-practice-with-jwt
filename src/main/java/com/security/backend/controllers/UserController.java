package com.security.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.security.backend.dtos.UserDto;
import com.security.backend.dtos.Request.EmailRequest;
import com.security.backend.dtos.Response.ApiResponse;
import com.security.backend.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        return ApiResponse.success(200, userService.getAllUsers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users")
    public ResponseEntity<ApiResponse<Object>> deleteUserByemail(@Valid @RequestBody EmailRequest request) {
        userService.deleteUserByEmail(request.getEmail());
        return ApiResponse.success("User deleted with email : " + request.getEmail(), 200, null);
    }

}
