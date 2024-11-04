package com.security.backend.dtos;

import java.util.Set;

import com.security.backend.models.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;

    @Email(message = "Provide a valid email.")
    @NotBlank(message = "Please enter an email.")
    private String email;

    @NotBlank(message = "Please enter first name.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "first name must contain only letters A-Z and a-z.")
    @Size(min = 3, max = 15, message = "name must be at least 3 and at max 15 chars long.")
    private String firstName;

    @NotBlank(message = "Please enter last name.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "last name must contain only letters A-Z and a-z.")
    @Size(min = 3, max = 15, message = "name must be at least 3 and at max 15 chars long.")
    private String lastName;

    @NotBlank(message = "You must provide a password.")
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    private String password;

    // @Past(message = "Date of birth must be in the past.")
    // @NotNull(message = "Date of birth is required.")
    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone
    // = "UTC")
    // private Date dob;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in the format yyyy-MM-dd")
    private String dob;

    // private String roles;
    Set<Role> roles;

}
