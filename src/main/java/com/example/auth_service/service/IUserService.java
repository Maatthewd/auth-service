package com.example.auth_service.service;


import com.example.auth_service.dto.response.MeResponse;
import org.springframework.security.core.Authentication;

public interface IUserService {

    MeResponse me(Authentication authentication);
}
