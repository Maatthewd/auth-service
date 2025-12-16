package com.example.auth_service.security;

import com.example.auth_service.domain.exception.InvalidJwtTokenException;
import com.example.auth_service.exception.ApiError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException
    {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ApiError error;

        if (authException instanceof InvalidJwtTokenException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            error = new ApiError(HttpStatus.UNAUTHORIZED, "INVALID_JWT", ex.getMessage());

        } // Aca se pueden agregar mas excepciones que puedan saltar en el filtro con un else-if

        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            error = new ApiError(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", authException.getMessage());
        }

        objectMapper.writeValue(response.getWriter(), error);
    }
}
