package com.example.auth_service.service;

import com.example.auth_service.domain.exception.InvalidJwtTokenException;
import com.example.auth_service.domain.model.Authority;
import com.example.auth_service.dto.request.TokenRequest;
import com.example.auth_service.dto.request.UserRequest;


import java.util.Set;

public interface IJwtService {

    String generateAccessToken(UserRequest request);

    String generateRefreshToken(UserRequest request);

    boolean isTokenValid(TokenRequest token);

    String extractUsername(TokenRequest token);

    Set<Authority> extractAuthorities(TokenRequest token);

    String extractTokenType(TokenRequest token);

    void validateAccessToken(TokenRequest token) throws InvalidJwtTokenException;

    void validateRefreshToken(TokenRequest token) throws InvalidJwtTokenException;

    void validateAdminToken(TokenRequest token) throws InvalidJwtTokenException;

    boolean isRefreshToken(TokenRequest token);
}
