package com.example.auth_service.service.impl;

import com.example.auth_service.config.PasswordConfig;
import com.example.auth_service.domain.exception.UserNotFoundException;
import com.example.auth_service.domain.exception.UsernameAlreadyExistsException;
import com.example.auth_service.domain.model.Role;
import com.example.auth_service.domain.model.User;
import com.example.auth_service.dto.request.CreateUserRequest;
import com.example.auth_service.dto.request.UpdateSelfRequest;
import com.example.auth_service.dto.request.UpdateUserRequest;
import com.example.auth_service.dto.response.MeResponse;
import com.example.auth_service.dto.response.UserResponse;
import com.example.auth_service.mapper.MeResponseMapper;
import com.example.auth_service.mapper.UserResponseMapper;
import com.example.auth_service.repository.AuthRepository;
import com.example.auth_service.service.IUserService;
import com.sun.jdi.request.InvalidRequestStateException;
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
                .orElseThrow(() -> handleUserNotFoundException());

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
    public UserResponse updateUser(Long id, UpdateUserRequest request) {

        User user = authRepository.findById(id)
                .orElseThrow(() -> handleUserNotFoundException());

        BCryptPasswordEncoder encoder = passwordConfig.passwordEncoder();

        if(request.hasUsername()) {

            if(request.username().equals(user.getUsername())) {
                throw new UsernameAlreadyExistsException("El usuario ya tiene ese nombre");
            }

            if(authRepository.existsByUsername(request.username())) {
                throw new UsernameAlreadyExistsException("El usuario ya existe");
            }

            user.setUsername(request.username());
        }

        String encodedPassword = encoder.encode(request.password());

        if(request.hasPassword()){

            if (encodedPassword.equals(user.getPassword())){
                throw new InvalidRequestStateException("Las contraseñas no pueden ser iguales");
            }

            user.setPassword(encodedPassword);
        }

        if(request.hasRoles()) {
                if(request.roles().containsAll(user.getRoles())) {
                    throw new InvalidRequestStateException("El usuario ya tiene estos roles");
                }

                user.setRoles(request.roles());
        }

        if(request.hasEnabled()) {

            if(request.enabled().equals(user.getEnabled())) {
                throw new InvalidRequestStateException("El usuario ya esta activado");
            }

            user.setEnabled(request.enabled());
        }

        user.setUpdatedAt(LocalDateTime.now());
        authRepository.save(user);

        return userResponseMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateSelf(Authentication authentication, UpdateSelfRequest request) {

        User user = authRepository.findByUsername(authentication.getName())
                .orElseThrow();

        BCryptPasswordEncoder encoder = passwordConfig.passwordEncoder();

        if(request.hasUsername()) {

            if(request.username().equals(user.getUsername())) {
                throw new UsernameAlreadyExistsException("El usuario ya tiene ese nombre");
            }

            if(authRepository.existsByUsername(request.username())) {
                throw new UsernameAlreadyExistsException("El usuario ya existe");
            }

            user.setUsername(request.username());
        }

        String encodedPassword = encoder.encode(request.password());

        if(request.hasPassword()){

            if (encodedPassword.equals(user.getPassword())){
                throw new InvalidRequestStateException("Las contraseñas no pueden ser iguales");
            }

            user.setPassword(encodedPassword);
        }

        user.setUpdatedAt(LocalDateTime.now());
        authRepository.save(user);

        return userResponseMapper.toUserResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = authRepository.findById(id)
                .orElseThrow(() -> handleUserNotFoundException());

        authRepository.delete(user);
    }

    @Override
    public void deleteSelf(Authentication authentication) {

        String username = authentication.getName();

        User user = authRepository.findByUsername(username)
                .orElseThrow(() -> handleUserNotFoundException());

        authRepository.delete(user);
    }

    private UserNotFoundException handleUserNotFoundException(){
        return new UserNotFoundException("Usuario no encontrado");
    }
}
