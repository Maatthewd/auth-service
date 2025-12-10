package com.example.auth_service.controller;


import com.example.auth_service.dto.response.UserResponse;
import com.example.auth_service.mapper.UserResponseMapper;
import com.example.auth_service.service.impl.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserResponseMapper userResponseMapper;

    @PreAuthorize("hasAnyAuthority('READ_USERS', 'ADMIN_AUTHORITY')")
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> allUsers() {

        List<UserResponse> response = authService.allUsers();
        return ResponseEntity.ok(response);

    }
}
