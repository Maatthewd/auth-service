package com.example.auth_service.service.impl;

import com.example.auth_service.domain.exception.WrongCredentialsException;
import com.example.auth_service.service.IPasswordVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordVerificationService implements IPasswordVerificationService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void verify(String rawPassword, String encodedPassword) {

        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new WrongCredentialsException("Credenciales inválidas");
        }
    }
}
