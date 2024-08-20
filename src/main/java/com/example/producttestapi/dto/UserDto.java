package com.example.producttestapi.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class UserDto {
    @NotEmpty(message = "User's first name is required")
    @Length(min = 2, max = 20, message = "User's first name can't be less than 2 characters and more than 25 characters")
    private String firstName;
    @NotEmpty(message = "User's last name is required")
    @Length(min = 2, max = 20, message = "User's last name can't be less than 2 characters and more than 25 characters")
    private String lastName;
    @NotEmpty(message = "User's email name is required")
    private String email;
}
