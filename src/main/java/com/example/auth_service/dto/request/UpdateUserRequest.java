package com.example.auth_service.dto.request;

import com.example.auth_service.domain.model.Role;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdateUserRequest(
        String username,

        @Size(min = 6, max = 18, message = "La contraseña debe tener entre 6 y 18 caracteres")
        String password,

        Set<Role> roles,

        Boolean enabled
) {

    public boolean hasUsername(){
        return username != null && !username.isBlank();
    }

    public boolean hasPassword(){
        return password != null && !password.isBlank();
    }

    public boolean hasRoles(){
        return roles != null && !roles.isEmpty();
    }

    public boolean hasEnabled(){
        return enabled != null;
    }

}
