package com.example.auth_service.service;

import com.example.auth_service.dto.request.AccessTokenRequest;
import com.example.auth_service.dto.request.LoginRequest;
import com.example.auth_service.dto.request.RefreshTokenRequest;
import com.example.auth_service.dto.request.RegisterRequest;
import com.example.auth_service.dto.response.AuthResponse;
import com.example.auth_service.dto.response.MeResponse;
import com.example.auth_service.dto.response.NewRefreshTokenResponse;
import com.example.auth_service.dto.response.UserResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IAuthService {

    void register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    NewRefreshTokenResponse refreshToken(RefreshTokenRequest request);

    MeResponse getMe(Authentication auth);

    List<UserResponse> allUsers();
}
