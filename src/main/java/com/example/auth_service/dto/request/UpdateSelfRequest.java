package com.example.auth_service.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateSelfRequest(

        String username,

        @Size(min = 6, max = 18,  message = "La contraseña debe tener entre 6 y 18 caracteres")
        String password
) {

    public boolean hasUsername(){
        return username != null && !username.isBlank();
    }

    public boolean hasPassword(){
        return password != null && !password.isBlank();
    }

}

