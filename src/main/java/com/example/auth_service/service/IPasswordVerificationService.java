package com.example.auth_service.service;

public interface IPasswordVerificationService {
      void verify(String rawPassword, String encodedPassword);
}
