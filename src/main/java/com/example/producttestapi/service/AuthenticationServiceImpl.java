package com.example.producttestapi.service;

import com.example.producttestapi.model.RegistrationRequest;
import com.example.producttestapi.exception.DuplicateResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    public AuthenticationServiceImpl(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(RegistrationRequest request) {
        logger.info("Registering new user with email:[{}]", request.getEmail());
        if(userService.UserExistsByEmail(request.getEmail())){
            logger.warn("User with this email [{}] already exists", request.getEmail());
            throw new DuplicateResourceException("Email is connected to another account.");
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.createUser(request);
        logger.info("Registered new user with email:[{}]", request.getEmail());
    }
}
