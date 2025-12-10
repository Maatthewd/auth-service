package com.example.auth_service.controller;


import com.example.auth_service.domain.model.User;
import com.example.auth_service.service.impl.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class UserController {

    @Autowired
    private AuthService authService;

    @PreAuthorize("hasAuthority('READ_USERS')")
    @GetMapping("/users")
    public List<User> allUsers() {
        return authService.allUsers();
    }
}
