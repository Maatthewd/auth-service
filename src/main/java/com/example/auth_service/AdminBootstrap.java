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
            createCustomUser("admin", "1234", Set.of(Role.ROLE_ADMIN));
        }




    }

    private User createCustomUser(String username, String password, Set<Role> roles) {

        User customUser = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(roles)
                .enabled(true)
                .build();

        return authRepository.save(customUser);
    }
}
