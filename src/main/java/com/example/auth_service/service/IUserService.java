package com.example.auth_service.service;


import com.example.auth_service.dto.request.CreateUserRequest;
import com.example.auth_service.dto.request.UpdateSelfRequest;
import com.example.auth_service.dto.request.UpdateUserRequest;
import com.example.auth_service.dto.response.MeResponse;
import com.example.auth_service.dto.response.UserResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IUserService {

    MeResponse me(Authentication authentication);

    List<UserResponse> allUsers();

    UserResponse createUser(CreateUserRequest request);

    UserResponse updateUser(Long id, UpdateUserRequest request);

    UserResponse updateSelf(Authentication authentication, UpdateSelfRequest request);

    void deleteUser(Long id);

    void deleteSelf(Authentication authentication);

}
