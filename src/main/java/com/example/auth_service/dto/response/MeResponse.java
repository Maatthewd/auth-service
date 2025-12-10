package com.example.auth_service.dto.response;

import com.example.auth_service.domain.model.Authority;
import com.example.auth_service.domain.model.Role;

import java.util.Set;

public record MeResponse(
        String username,
        Set<Role> rolesSet,
        Set<Authority> authoritySet
) {
}
