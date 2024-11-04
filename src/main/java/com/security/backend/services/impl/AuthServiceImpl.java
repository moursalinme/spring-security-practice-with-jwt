package com.security.backend.services.impl;

import java.text.ParseException;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.backend.dtos.UserDto;
import com.security.backend.dtos.Request.LoginRequest;
import com.security.backend.mappers.ModelMapper;
import com.security.backend.models.Role;
import com.security.backend.repositories.RoleRepository;
import com.security.backend.services.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    private Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public UserDto handleRegister(UserDto user) throws Exception {
        try {
            user = userService.registerUser(user);
        } catch (ParseException e) {
            throw new Exception("Failed to parse date. try again with format yyyy-MM-dd.");
        }
        addPrincipalToSecurityContext(user);
        return user;
    }

    @Override
    public UserDto handleLogin(LoginRequest loginRequest) {
        UserDto user = userService.getUserByEmail(loginRequest.getEmail());
        if (user == null ||
                !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password.");
        }
        addPrincipalToSecurityContext(user);
        return user;
    }

    private void addPrincipalToSecurityContext(UserDto user) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                AuthorityUtils.createAuthorityList(
                        ModelMapper.toListOfRoleString(user.getRoles())));

        logger.info(authToken.toString());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    @Override
    public UserDto makeAdminWithEmail(String email) throws ParseException {
        UserDto user = userService.getUserByEmail(email);
        logger.info("Found user with mail is -> {}", user.toString());
        Role admin = roleRepository.findByName("ADMIN");
        if (user.getRoles() == null)
            user.setRoles(new HashSet<Role>());
        user.getRoles().add(admin);
        user = userService.updateUserRoles(email, user.getRoles());
        return user;
    }

}
