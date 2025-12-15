package com.example.auth_service.service;

import com.example.auth_service.domain.exception.InvalidJwtTokenException;
import com.example.auth_service.domain.model.Authority;
import com.example.auth_service.dto.request.TokenRequest;
import com.example.auth_service.dto.request.UserRequest;
import com.example.auth_service.dto.response.RefreshTokenResponse;


import java.util.Set;
import java.util.jar.JarException;

public interface IJwtService {

    String generateAccessToken(UserRequest request);

    RefreshTokenResponse generateRefreshToken(UserRequest request);

    boolean isTokenValid(TokenRequest token);

    void validateTokenOrThrow(TokenRequest token) throws JarException;

    String extractUsername(TokenRequest token);

    Set<Authority> extractAuthorities(TokenRequest token);

    String extractTokenType(TokenRequest token);

    boolean isRefreshToken(TokenRequest token);

    void validateAccessToken(TokenRequest token) throws InvalidJwtTokenException;

    void validateRefreshToken(TokenRequest token) throws InvalidJwtTokenException;

    void validateAdminToken(TokenRequest token) throws InvalidJwtTokenException;

}
