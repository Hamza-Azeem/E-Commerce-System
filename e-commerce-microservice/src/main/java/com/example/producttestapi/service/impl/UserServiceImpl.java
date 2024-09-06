package com.example.producttestapi.service.impl;

import com.example.producttestapi.client.EmailClient;
import com.example.producttestapi.dto.UserDto;
import com.example.producttestapi.model.EmailRequest;
import com.example.producttestapi.model.RegistrationRequest;
import com.example.producttestapi.entities.Role;
import com.example.producttestapi.entities.User;
import com.example.producttestapi.repos.UserRepo;
import com.example.producttestapi.service.RoleService;
import com.example.producttestapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.producttestapi.mapper.UserMapper.convertToUserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final RoleService roleService;
    private final EmailClient emailClient;

    @Override
    public void createUser(RegistrationRequest request) {
        User user = new User(request.getFirstName(),
                request.getLastName(),
                request.getPassword(),
                request.getEmail());
        Role userRole = roleService.findRoleByName("USER");
        if(userRole == null){
            userRole = roleService.saveRole(new Role("USER"));
        }
        user.addRole(userRole);
        EmailRequest emailRequest = EmailRequest.builder()
                .firstname(request.getFirstName())
                .to(request.getEmail())
                .build();
        emailClient.sendRegistrationEmail(emailRequest);
        userRepo.save(user);
    }

    @Override
    public UserDto findUserDtoByEmail(String email) {
        return convertToUserDto(userRepo.findByEmail(email)) ;
    }
    @Override
    public User findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public boolean UserExistsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(user -> convertToUserDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public void updateUserCart(User user) {
        userRepo.save(user);
    }
}
