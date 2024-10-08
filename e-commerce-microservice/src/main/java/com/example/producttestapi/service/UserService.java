package com.example.producttestapi.service;

import com.example.producttestapi.dto.UserDto;
import com.example.producttestapi.model.RegistrationRequest;
import com.example.producttestapi.entities.User;

import java.util.List;

public interface UserService {
    public void createUser(RegistrationRequest request);
    public User findUserByEmail(String email);
    public UserDto findUserDtoByEmail(String email);
    public boolean UserExistsByEmail(String email);
    public List<UserDto> findAllUsers();
    public void updateUserCart(User user);
}
