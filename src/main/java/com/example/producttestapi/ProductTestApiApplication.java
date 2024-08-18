package com.example.producttestapi;

import com.example.producttestapi.entities.Role;
import com.example.producttestapi.entities.User;
import com.example.producttestapi.repos.RoleRepo;
import com.example.producttestapi.repos.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication

public class ProductTestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductTestApiApplication.class, args);
    }
    @Bean
    public CommandLineRunner commandLineRunner(RoleRepo roleRepo, UserRepo userRepo, PasswordEncoder passwordEncoder){
        return args -> {
            if(roleRepo.findByName("ADMIN") == null){
                Role role = new Role("ADMIN");
                roleRepo.save(role);
            }
            if(userRepo.findByEmail("admin@example.com") == null){
                User user = User.builder()
                        .email("admin@example.com")
                        .firstName("admin")
                        .password(passwordEncoder.encode("admin"))
                        .build();
                user.addRole(roleRepo.findByName("ADMIN"));
                userRepo.save(user);
            }
        };
    }

}
