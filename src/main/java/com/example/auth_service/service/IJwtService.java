package com.example.auth_service.service;

import com.example.auth_service.domain.model.Authority;
import com.example.auth_service.domain.model.Role;

import java.util.Set;

public interface IJwtService {

    String generateToken(String username, Set<Role> roles);

    boolean isTokenValid(String token);

    String extractUsername(String token);

    Set<Authority> extractAuthorities(String token);
}
