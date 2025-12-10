package com.example.auth_service.security;

import com.example.auth_service.domain.exception.InvalidJwtTokenException;
import com.example.auth_service.domain.exception.InvalidTokenException;
import com.example.auth_service.domain.model.Authority;
import com.example.auth_service.domain.model.Role;
import com.example.auth_service.dto.request.TokenRequest;
import com.example.auth_service.exception.ApiError;
import com.example.auth_service.service.impl.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {


        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        TokenRequest tokenRequest = new TokenRequest(token);

        try {
            if (!jwtService.isTokenValid(tokenRequest)) {
                sendError(response, "InvalidToken", "Token inválido o expirado");
                return;
            }

            // Validamos que sea access token
            jwtService.validateAccessToken(tokenRequest);

            // Validamos que el token contenga READ_USERS
            jwtService.validateAdminToken(tokenRequest);


            String username = jwtService.extractUsername(tokenRequest);
            Set<Authority> authorities = jwtService.extractAuthorities(tokenRequest);



            List<SimpleGrantedAuthority> grantedAuthorities =
                    authorities.stream().map(a -> new SimpleGrantedAuthority(a.name())).toList();

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities)
            );

            filterChain.doFilter(request, response);

        } catch (InvalidJwtTokenException e) {
            sendError(response, "InvalidToken", e.getMessage());
        }
    }


    private void sendError(HttpServletResponse response, String error, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, error, message);
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(apiError));
    }

}
