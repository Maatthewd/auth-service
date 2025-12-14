package com.example.auth_service.dto.request;

import com.example.auth_service.domain.model.Role;

import java.util.List;

public record CreateUserRequest(
        String username,
        String password,
        List<Role> roles

) {
}
