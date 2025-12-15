package com.example.auth_service.service.impl;

import com.example.auth_service.domain.exception.InvalidTokenException;
import com.example.auth_service.domain.exception.UserNotFoundException;
import com.example.auth_service.domain.exception.WrongCredentialsException;
import com.example.auth_service.domain.model.User;
import com.example.auth_service.dto.request.*;
import com.example.auth_service.domain.exception.UsernameAlreadyExistsException;
import com.example.auth_service.dto.response.*;
import com.example.auth_service.repository.AuthRepository;
import com.example.auth_service.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.Instant;

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


        AccessTokenResponse accessToken = jwtService.generateAccessToken(userRequest);
        RefreshTokenResponse refreshToken = jwtService.generateRefreshToken(userRequest);



        user.setRefreshToken(refreshToken.refreshToken());
        user.setRefreshTokenExpiry(refreshToken.refreshTokenExpiry());

        authRepository.save(user);

        return new AuthResponse(accessToken.accessToken(),
                refreshToken.refreshToken());
    }

    @Override
    public NewRefreshTokenResponse refreshToken(TokenRequest request) {

        TokenRequest refreshToken = new TokenRequest(request.userToken());

        // 1. Verificar que la firma sea valida y no este expirado
        jwtService.validateTokenOrThrow(refreshToken);

        // 2. Verificar que sea un REFRESH token
        jwtService.validateRefreshToken(refreshToken);

        String username = jwtService.extractUsername(refreshToken);
        User user = authRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));


        // 3. Verificar que el refresh Token sea el mismo que el guardado

        if(!refreshToken.userToken().equals(user.getRefreshToken())) {
            throw new InvalidTokenException("Refresh token no valido o revocado");
        }

        UserRequest userRequest = new UserRequest(
                user.getUsername(),
                user.getRoles()
        );

        AccessTokenResponse newAccessToken = jwtService.generateAccessToken(userRequest);
        RefreshTokenResponse newRefreshToken = jwtService.generateRefreshToken(userRequest);

        user.setRefreshToken(newRefreshToken.refreshToken());
        user.setRefreshTokenExpiry(newRefreshToken.refreshTokenExpiry());
        authRepository.save(user);

        return new NewRefreshTokenResponse(newAccessToken.accessToken(),
                newRefreshToken.refreshToken(),
                newRefreshToken.refreshTokenExpiry()
        );
    }


}
