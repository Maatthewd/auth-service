package com.example.auth_service.security;

import com.example.auth_service.domain.exception.InvalidJwtTokenException;
import com.example.auth_service.exception.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que captura excepciones lanzadas por otros filtros (especialmente JWTAuthenticationFilter)
 * y las convierte en respuestas HTTP apropiadas.
 */
@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (InvalidJwtTokenException ex) {
            handleInvalidJwtToken(response, ex);
        }
    }

    private void handleInvalidJwtToken(HttpServletResponse response, InvalidJwtTokenException ex)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ApiError error = new ApiError(
                HttpStatus.UNAUTHORIZED,
                "INVALID_JWT",
                ex.getMessage()
        );

        objectMapper.writeValue(response.getWriter(), error);
    }
}