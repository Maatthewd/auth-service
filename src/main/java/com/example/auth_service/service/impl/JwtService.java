package com.example.auth_service.service.impl;

import com.example.auth_service.domain.exception.InvalidJwtTokenException;
import com.example.auth_service.domain.model.Authority;
import com.example.auth_service.domain.model.User;
import com.example.auth_service.dto.request.TokenRequest;
import com.example.auth_service.dto.request.UserRequest;
import com.example.auth_service.dto.response.AccessTokenResponse;
import com.example.auth_service.dto.response.RefreshTokenResponse;
import com.example.auth_service.service.IJwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
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
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public AccessTokenResponse generateAccessToken(User user) {

        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("authorities", user.getRoles().stream()
                        .flatMap(r -> r.getAuthorities().stream())
                        .map(Enum::name)
                        .toList())
                .claim("type", "ACCESS")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        return new AccessTokenResponse(token);
    }

    @Override
    public RefreshTokenResponse generateRefreshToken(User user) {

        Instant expiry = Instant.now().plusSeconds(refreshTokenExpiration * 24 * 3600);

        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("type", "REFRESH")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(expiry))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        return new RefreshTokenResponse(token, expiry);
    }

    @Override
    public boolean isTokenValid(TokenRequest token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token.userToken());
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    @Override
    public void validateTokenOrThrow(TokenRequest token) throws JwtException {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token.userToken());
        } catch (ExpiredJwtException e) {
            throw new InvalidJwtTokenException("El token ha expirado");
        } catch (UnsupportedJwtException e) {
            throw new InvalidJwtTokenException("Formato de token no soportado");
        } catch (MalformedJwtException e) {
            throw new InvalidJwtTokenException("Token mal formado");
        } catch (SignatureException e) {
            throw new InvalidJwtTokenException("Firma de token inválida");
        } catch (IllegalArgumentException e) {
            throw new InvalidJwtTokenException("Token vacío o inválido");
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

        if(!authorities.contains(Authority.ADMIN_AUTHORITY)) {
            throw new InvalidJwtTokenException("Se esperaba un ADMIN token");
        }
    }


    private Claims extractClaims(TokenRequest token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token.userToken())
                .getBody();
    }
}
