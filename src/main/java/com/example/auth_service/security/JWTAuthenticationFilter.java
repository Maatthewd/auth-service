package com.example.auth_service.security;

import com.example.auth_service.domain.model.Authority;
import com.example.auth_service.dto.request.TokenRequest;
import com.example.auth_service.service.impl.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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

        jwtService.validateTokenOrThrow(tokenRequest);

        jwtService.validateAccessToken(tokenRequest);

        String username = jwtService.extractUsername(tokenRequest);
        Set<Authority> authorities = jwtService.extractAuthorities(tokenRequest);

        List<SimpleGrantedAuthority> grantedAuthorities =
                authorities.stream().map(a -> new SimpleGrantedAuthority(a.name())).toList();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities)
        );

        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return path.startsWith("/auth/login")
                || path.startsWith("/auth/register")
                || path.startsWith("/auth/refresh-token");
    }
}