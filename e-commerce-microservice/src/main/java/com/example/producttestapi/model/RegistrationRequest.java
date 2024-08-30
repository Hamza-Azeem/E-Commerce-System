package com.example.producttestapi.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    @NotEmpty(message = "User's first name is required")
    @Length(min = 2, max = 20, message = "User's first name can't be less than 2 characters and more than 25 characters")
    private String firstName;
    @NotEmpty(message = "User's last name is required")
    @Length(min = 2, max = 20, message = "User's last name can't be less than 2 characters and more than 25 characters")
    private String lastName;
    @NotEmpty(message = "User's email name is required")
    private String email;
    @NotEmpty(message = "User's password is required")
    @Length(min = 8, max = 100, message = "Your password can't be less than 8 characters and more than 100 character")
    private String password;

}
