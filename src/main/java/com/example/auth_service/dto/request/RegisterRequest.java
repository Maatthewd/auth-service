package com.example.auth_service.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(

        @NotNull String username,

        @NotNull @DecimalMax("15")
        String password
) {
}
