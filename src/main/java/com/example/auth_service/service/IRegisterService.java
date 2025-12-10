package com.example.auth_service.service;

import com.example.auth_service.domain.model.User;

public interface IRegisterService {

    User registerUser(String username, String password);
}
