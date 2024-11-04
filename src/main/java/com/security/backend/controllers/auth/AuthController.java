package com.security.backend.controllers.auth;

import java.util.HashMap;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.security.backend.config.JWT.JwtService;
import com.security.backend.dtos.UserDto;
import com.security.backend.dtos.Request.LoginRequest;
import com.security.backend.dtos.Response.ApiResponse;
import com.security.backend.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> registerUser(@RequestBody @Valid UserDto user,
            HttpServletRequest request) throws Exception {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user = authService.handleRegister(user);
        } catch (DataIntegrityViolationException ex) {
            return ApiResponse.failure(
                    request.getRequestURI(),
                    "Email already registerd or something wrong with the data while saving in database.", 409, null);
        } catch (Exception ex) {
            return ApiResponse.failure(request.getRequestURI(),
                    ex.getMessage() == null ? "Something went wrong." : ex.getMessage(), 500, null);
        }

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + jwtService.generateToken(user));

        return ApiResponse.successWithHeaders(request.getRequestURI(), "User Registration successful.", 201,
                user, header);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> handleLogin(@RequestBody LoginRequest loginReq,
            HttpServletRequest request) {
        UserDto user;
        try {
            user = authService.handleLogin(loginReq);
        } catch (BadCredentialsException ex) {
            return ApiResponse.failure(
                    request.getRequestURI(),
                    ex.getMessage(), 401, null);
        } catch (Exception ex) {
            return ApiResponse.failure(request.getRequestURI(),
                    ex.getMessage() == null ? "Something went wrong." : ex.getMessage(), 500, null);
        }
        HashMap<String, String> token = new HashMap<>();
        token.put("jwtToken", "Bearer " + jwtService.generateToken(user));
        return ApiResponse.success(request.getRequestURI(), "User Login successful.", HttpStatus.OK.value(),
                token);
    }

}
