package com.example.auth_service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotNull String username,

        @NotNull @Size(min = 6, max=18)
        String password
) {
}
