package com.security.backend.services.impl;

import java.text.ParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.security.backend.dtos.UserDto;
import com.security.backend.mappers.ModelMapper;
import com.security.backend.models.Role;
import com.security.backend.models.UserModel;
import com.security.backend.repositories.UserRepository;
import com.security.backend.services.RoleService;
import com.security.backend.services.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    public UserDto getUserByEmail(String email) {
        UserModel user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with Email : " + email.toString());
        }
        return ModelMapper.toUserDto(user);
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

    @Override
    @Transactional
    public boolean deleteUserByEmail(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new UsernameNotFoundException("Email not valid......");
        }
        userRepository.deleteByEmail(email);
        return true;
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDto updateUserRoles(String email, Set<Role> roles) throws ParseException {
        UserModel user = getUserModelByEmail(email);
        user.setRoles(roles);
        userRepository.save(user);
        return ModelMapper.toUserDto(user);
    }

    private UserModel getUserModelByEmail(String email) {
        UserModel user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email : " + email);
        }
        return user;
    }

}
