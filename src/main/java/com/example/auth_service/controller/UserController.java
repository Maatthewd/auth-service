package com.example.auth_service.controller;


import com.example.auth_service.dto.response.MeResponse;
import com.example.auth_service.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyAuthority('READ_SELF')")
    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(Authentication authentication) {

        MeResponse response = userService.me(authentication);

        return ResponseEntity.ok(response);
    }


}
