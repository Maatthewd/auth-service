package com.example.auth_service.service.impl;

import com.example.auth_service.domain.exception.WrongCredentialsException;
import com.example.auth_service.domain.model.User;
import com.example.auth_service.dto.request.LoginRequest;
import com.example.auth_service.dto.request.RegisterRequest;
import com.example.auth_service.domain.exception.UsernameAlreadyExistsException;
import com.example.auth_service.dto.response.AuthResponse;
import com.example.auth_service.repository.AuthRepository;
import com.example.auth_service.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

        String token = jwtService.generateToken(
                user.getUsername(),
                user.getRoles()
        );

        return new AuthResponse(token);

    }


    @Override
    public List<User> allUsers() {
        return authRepository.findAll();
    }
}
