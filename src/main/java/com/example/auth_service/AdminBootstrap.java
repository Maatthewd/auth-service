package com.example.auth_service;

import com.example.auth_service.domain.model.Role;
import com.example.auth_service.domain.model.User;
import com.example.auth_service.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AdminBootstrap implements CommandLineRunner {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public void run(String... args){

        boolean adminExists =
                authRepository.existsByRole(Role.ROLE_ADMIN);

        if(!adminExists){

            String username = "admin";
            String password = "1234";
            Set<Role> userRole = Set.of(Role.ROLE_ADMIN);

            User adminUser = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .roles(userRole)
                    .enabled(true)
                    .build();

            authRepository.save(adminUser);
        }
    }
}
