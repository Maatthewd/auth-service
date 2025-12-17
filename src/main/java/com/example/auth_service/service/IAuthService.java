package com.example.auth_service.service;

import com.example.auth_service.dto.request.LoginRequest;
import com.example.auth_service.dto.request.RegisterRequest;
import com.example.auth_service.dto.request.TokenRequest;
import com.example.auth_service.dto.response.AuthResponse;
import com.example.auth_service.dto.response.NewRefreshTokenResponse;

public interface IAuthService {

    void register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    NewRefreshTokenResponse refreshToken(TokenRequest request);

}
