package com.example.auth_service.service;

import com.example.auth_service.domain.exception.InvalidJwtTokenException;
import com.example.auth_service.domain.model.Authority;
import com.example.auth_service.dto.request.AccessTokenRequest;
import com.example.auth_service.dto.request.UserRequest;
import com.example.auth_service.dto.response.RefreshTokenResponse;


import java.util.Set;
import java.util.jar.JarException;

public interface IJwtService {

    String generateAccessToken(UserRequest request);

    RefreshTokenResponse generateRefreshToken(UserRequest request);

    boolean isTokenValid(AccessTokenRequest token);

    void validateTokenOrThrow(AccessTokenRequest token) throws JarException;

    String extractUsername(AccessTokenRequest token);

    Set<Authority> extractAuthorities(AccessTokenRequest token);

    String extractTokenType(AccessTokenRequest token);

    boolean isRefreshToken(AccessTokenRequest token);

    void validateAccessToken(AccessTokenRequest token) throws InvalidJwtTokenException;

    void validateRefreshToken(AccessTokenRequest token) throws InvalidJwtTokenException;

    void validateAdminToken(AccessTokenRequest token) throws InvalidJwtTokenException;

}
