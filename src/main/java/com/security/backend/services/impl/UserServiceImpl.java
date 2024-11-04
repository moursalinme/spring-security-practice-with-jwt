package com.security.backend.services.impl;

import java.text.ParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.security.backend.dtos.UserDto;
import com.security.backend.mappers.ModelMapper;
import com.security.backend.models.UserModel;
import com.security.backend.repositories.UserRepository;
import com.security.backend.services.RoleService;
import com.security.backend.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    public UserModel getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDto registerUser(UserDto user) throws ParseException {
        user.setRoles(Set.of(roleService.getRoleByName("USER")));
        UserModel userModel = ModelMapper.toUserModel(user);
        return ModelMapper.toUserDto(userRepository.save(userModel));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserModel> users = userRepository.findAll();
        return users.stream()
                .map(user -> ModelMapper.toUserDto(user))
                .collect(Collectors.toList());
    }
}
