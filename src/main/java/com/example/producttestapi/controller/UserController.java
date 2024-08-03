package com.example.producttestapi.controller;

import com.example.producttestapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    private ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }
    @GetMapping("/{email}")
    private ResponseEntity<?> findUserByEmail(@PathVariable String email){
        return ResponseEntity.ok(userService.findUserDtoByEmail(email));
    }
}
