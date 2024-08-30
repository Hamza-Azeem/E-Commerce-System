package com.example.producttestapi.mapper;

import com.example.producttestapi.dto.UserDto;
import com.example.producttestapi.entities.User;

public class UserMapper {

    public static User convertToUser(UserDto userDto){
        return User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .build();
    }
    public static UserDto convertToUserDto(User user){
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }
}
