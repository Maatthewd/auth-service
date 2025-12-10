package com.example.auth_service.service.impl;

import com.example.auth_service.dto.response.UserResponse;
import com.example.auth_service.mapper.UserResponseMapper;
import com.example.auth_service.repository.AuthRepository;
import com.example.auth_service.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService implements IAdminService {

    @Autowired
    private AuthRepository authRepository;

    @Override
    public List<UserResponse> allUsers() {
        return authRepository
                .findAll()
                .stream()
                .map(UserResponseMapper::toUserResponse)
                .toList();
    }
}
