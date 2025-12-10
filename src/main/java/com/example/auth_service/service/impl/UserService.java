package com.example.auth_service.service.impl;

import com.example.auth_service.domain.exception.UserNotFoundException;
import com.example.auth_service.domain.model.User;
import com.example.auth_service.dto.response.MeResponse;
import com.example.auth_service.mapper.MeResponseMapper;
import com.example.auth_service.repository.AuthRepository;
import com.example.auth_service.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

public class UserService implements IUserService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private MeResponseMapper meResponseMapper;


    @Override
    public MeResponse me(Authentication auth) {
        String username = auth.getName();

        User user = authRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        return meResponseMapper.toMeResponse(user);
    }
}
