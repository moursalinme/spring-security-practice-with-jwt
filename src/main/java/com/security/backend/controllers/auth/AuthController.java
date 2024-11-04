package com.security.backend.controllers.auth;

import java.text.ParseException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.security.backend.config.JWT.JwtService;
import com.security.backend.dtos.UserDto;
import com.security.backend.dtos.Request.EmailRequest;
import com.security.backend.dtos.Request.LoginRequest;
import com.security.backend.dtos.Response.ApiResponse;
import com.security.backend.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final JwtService jwtService;
    private final static Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> registerUser(@RequestBody @Valid UserDto user) throws Exception {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = authService.handleRegister(user);

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + jwtService.generateToken(user));

        return ApiResponse.successWithHeaders("User Registration successful.", 201,
                user, header);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> handleLogin(@RequestBody LoginRequest loginReq) throws ParseException {
        UserDto user;
        try {
            user = authService.handleLogin(loginReq);
        } catch (UsernameNotFoundException ex) {
            throw new BadCredentialsException("Invalid email or password.");
        }
        HashMap<String, String> token = new HashMap<>();
        token.put("jwtToken", "Bearer " + jwtService.generateToken(user));
        return ApiResponse.success("User Login successful.", HttpStatus.OK.value(),
                token);
    }

    @GetMapping("/getMyAuth")
    public ResponseEntity<ApiResponse<Object>> getMyAuthDetails(Authentication authentication) {
        logger.info(authentication.toString());
        return ApiResponse.success(200, authentication);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/makeAdmin")
    public ResponseEntity<ApiResponse<Object>> addAdminAuth(@RequestBody EmailRequest request) throws ParseException {
        UserDto user = authService.makeAdminWithEmail(request.getEmail());
        return ApiResponse.success(200, user);
    }

}
