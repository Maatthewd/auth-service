package com.example.auth_service.service.impl;

import com.example.auth_service.domain.exception.UserNotFoundException;
import com.example.auth_service.domain.exception.WrongCredentialsException;
import com.example.auth_service.domain.model.User;
import com.example.auth_service.dto.request.*;
import com.example.auth_service.domain.exception.UsernameAlreadyExistsException;
import com.example.auth_service.dto.response.*;
import com.example.auth_service.mapper.MeResponseMapper;
import com.example.auth_service.mapper.UserResponseMapper;
import com.example.auth_service.repository.AuthRepository;
import com.example.auth_service.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordVerificationService passwordVerificationService;

    @Override
    public void register(RegisterRequest request) {

        String username = request.username();
        String password = request.password();

        if(authRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("El usuario ya existe");
        }

        registerService.registerUser(username, password);

    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = authRepository.findByUsername(request.username())
                .orElseThrow(() -> new WrongCredentialsException("Credenciales inválidas"));

        passwordVerificationService.verify(
                request.password(),
                user.getPassword()
        );

        UserRequest userRequest = new UserRequest(
                user.getUsername(),
                user.getRoles()
        );


        String accessToken = jwtService.generateAccessToken(userRequest);
        String refreshToken = jwtService.generateRefreshToken(userRequest).refreshToken();

        user.setRefreshToken(refreshToken);
        authRepository.save(user);

        return new AuthResponse(accessToken, refreshToken);

    }

    @Override
    public NewRefreshTokenResponse refreshToken(RefreshTokenRequest request) {

        AccessTokenRequest refreshToken = new AccessTokenRequest(request.refreshToken());

        // 1. Verificar que la firma sea valida y no este expirado
        jwtService.validateTokenOrThrow(refreshToken);

        // 2. Verificar que sea un REFRESH token
        jwtService.validateRefreshToken(refreshToken);

        String username = jwtService.extractUsername(refreshToken);
        User user = authRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        UserRequest userRequest = new UserRequest(
                user.getUsername(),
                user.getRoles()
        );

        String newAccessToken = jwtService.generateAccessToken(userRequest);
        RefreshTokenResponse newRefreshToken = jwtService.generateRefreshToken(userRequest);

        user.setRefreshToken(newRefreshToken.refreshToken());
        authRepository.save(user);

        return new NewRefreshTokenResponse(newAccessToken, newRefreshToken.refreshToken(), newRefreshToken.refreshTokenExpiry());
    }


}
