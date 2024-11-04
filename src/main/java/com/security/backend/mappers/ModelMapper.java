package com.security.backend.mappers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.security.backend.dtos.UserDto;
import com.security.backend.models.Role;
import com.security.backend.models.UserModel;

@Component
public class ModelMapper {

    public static UserDto toUserDto(UserModel user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles())
                .dob(new SimpleDateFormat("yyyy-MM-dd").format(user.getDob()))
                .build();
    }

    public static UserModel toUserModel(UserDto user) throws ParseException {
        return UserModel.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles())
                .dob(new SimpleDateFormat("yyyy-MM-dd").parse(user.getDob()))
                .build();
    }

    public static List<String> toListOfRoleString(Set<Role> roles) {
        return roles.stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());
    }
}
