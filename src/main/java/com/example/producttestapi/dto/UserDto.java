package com.example.producttestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
}
