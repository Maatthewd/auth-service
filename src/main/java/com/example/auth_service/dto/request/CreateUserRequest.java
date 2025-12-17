package com.example.auth_service.dto.request;

import com.example.auth_service.domain.model.Role;


import java.util.Set;

public record CreateUserRequest(
        String username,
        String password,
        Set<Role> roles

) {
}
