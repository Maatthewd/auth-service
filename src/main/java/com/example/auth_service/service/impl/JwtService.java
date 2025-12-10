package com.example.auth_service.service.impl;

import com.example.auth_service.domain.exception.InvalidJwtTokenException;
import com.example.auth_service.domain.model.Authority;
import com.example.auth_service.dto.request.TokenRequest;
import com.example.auth_service.dto.request.UserRequest;
import com.example.auth_service.service.IJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtService implements IJwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration-ms}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration-days}")
    private long refreshTokenExpiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateAccessToken(UserRequest request) {

        return Jwts.builder()
                .setSubject(request.username())
                .claim("authorities", request.roles().stream()
                        .flatMap(r -> r.getAuthorities().stream())
                        .map(Enum::name)
                        .toList())
                .claim("type", "ACCESS")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    @Override
    public String generateRefreshToken(UserRequest request) {

        return Jwts.builder()
                .setSubject(request.username())
                .claim("type", "REFRESH")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(refreshTokenExpiration * 24 * 3600)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(TokenRequest token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token.token());
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    @Override
    public String extractUsername(TokenRequest token) {
        return extractClaims(token).getSubject();
    }


    @Override
    public Set<Authority> extractAuthorities(TokenRequest token){

        List<String> authorities = extractClaims(token)
                .get("authorities", List.class);


        if (authorities == null) {
            return Set.of();
        }

        Set<Authority> authoritiesSet =
                authorities
                        .stream()
                        .map(Authority::valueOf)
                        .collect(Collectors.toSet()
                );

        return authoritiesSet;
    }

    @Override
        public String extractTokenType(TokenRequest token) {
        String tokenType = extractClaims(token)
                .get("type", String.class);

        return tokenType;
    }

    @Override
    public boolean isRefreshToken(TokenRequest token) {
        return "REFRESH".equals(extractTokenType(token));
    }

    public void validateAccessToken(TokenRequest token) throws InvalidJwtTokenException{
        Claims claims = extractClaims(token);
        String type = claims.get("type", String.class);
        if (!"ACCESS".equals(type)) {
            throw new InvalidJwtTokenException("Se esperaba un access token");
        }
    }

    public void validateRefreshToken(TokenRequest token) throws InvalidJwtTokenException {
        Claims claims = extractClaims(token);
        String type = claims.get("type", String.class);
        if (!"REFRESH".equals(type)) {
            throw new InvalidJwtTokenException("Se esperaba un refresh token");
        }
    }

    @Override
    public void validateAdminToken(TokenRequest token) throws InvalidJwtTokenException {
        Set<Authority> authorities = extractAuthorities(token);

        if(!authorities.contains(Authority.READ_USERS)) {
            throw new InvalidJwtTokenException("Se esperaba un ADMIN token");
        }
    }


    private Claims extractClaims(TokenRequest token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token.token())
                .getBody();
    }
}
