package com.example.auth_service.dto.response;

import java.time.Instant;

public record TokenResponse (
    String accessToken,
    String refreshToken,
    Instant refreshTokenExpiry
){
}
