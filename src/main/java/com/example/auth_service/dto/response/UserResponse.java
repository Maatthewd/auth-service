package com.example.auth_service.dto.response;

import com.example.auth_service.domain.model.Role;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(
        Long id,
        String username,
        Set<Role> roles,
        boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
