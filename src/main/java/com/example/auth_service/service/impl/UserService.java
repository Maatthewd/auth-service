package com.example.auth_service.service.impl;

import com.example.auth_service.config.PasswordConfig;
import com.example.auth_service.domain.exception.UserNotFoundException;
import com.example.auth_service.domain.exception.UsernameAlreadyExistsException;
import com.example.auth_service.domain.model.Role;
import com.example.auth_service.domain.model.User;
import com.example.auth_service.dto.request.CreateUserRequest;
import com.example.auth_service.dto.response.MeResponse;
import com.example.auth_service.dto.response.UserResponse;
import com.example.auth_service.mapper.MeResponseMapper;
import com.example.auth_service.mapper.UserResponseMapper;
import com.example.auth_service.repository.AuthRepository;
import com.example.auth_service.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements IUserService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordConfig passwordConfig;

    @Autowired
    private MeResponseMapper meResponseMapper;

    @Autowired
    private UserResponseMapper userResponseMapper;

    @Override
    public MeResponse me(Authentication auth) {
        String username = auth.getName();

        User user = authRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        return meResponseMapper.toMeResponse(user);
    }

    @Override
    public List<UserResponse> allUsers() {
        return authRepository
                .findAll()
                .stream()
                .map(u -> userResponseMapper.toUserResponse(u))
                .toList();
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        String username = request.username();
        String rawPassword = request.password();
        Set<Role> roles = request.roles();

        BCryptPasswordEncoder encoder = passwordConfig.passwordEncoder();
        String encodedPassword = encoder.encode(rawPassword);

        LocalDateTime creationDate = LocalDateTime.now();

        if(authRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("El usuario ya existe");
        }


        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .roles(roles)
                .enabled(true)
                .createdAt(creationDate)
                .updatedAt(creationDate)
                .build();

        authRepository.save(user);

        return userResponseMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateUser() {

        return null;
    }

    @Override
    public UserResponse deleteUser() {

        return null;
    }
}
