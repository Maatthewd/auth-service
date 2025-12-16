package com.example.auth_service.controller;


import com.example.auth_service.dto.request.CreateUserRequest;
import com.example.auth_service.dto.request.UserRequest;
import com.example.auth_service.dto.response.MeResponse;
import com.example.auth_service.dto.response.UserResponse;
import com.example.auth_service.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;


    @PreAuthorize("hasAuthority('READ_SELF')")
    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(Authentication authentication) {

        MeResponse response = userService.me(authentication);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('READ_USERS')")
    @GetMapping()
    public ResponseEntity<List<UserResponse>> allUsers() {

        List<UserResponse> response = userService.allUsers();
        return ResponseEntity.ok(response);

    }

    @PreAuthorize("hasAuthority('CREATE_USERS')")
    @PostMapping()
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request){

        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAuthority('DELETE_USERS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('DELETE_SELF')")
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteSelf(Authentication authentication) {

        userService.deleteSelf(authentication);
        return ResponseEntity.noContent().build();
    }


}
