package com.example.auth_service.service;

import com.example.auth_service.dto.response.UserResponse;

import java.util.List;

public interface IAdminService {

    List<UserResponse> allUsers();
}
