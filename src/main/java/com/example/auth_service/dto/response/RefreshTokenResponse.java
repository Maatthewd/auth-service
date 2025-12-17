package com.example.auth_service.dto.response;

import java.time.Instant;

public record RefreshTokenResponse(
        String refreshToken,
        Instant refreshTokenExpiry
) {
}
