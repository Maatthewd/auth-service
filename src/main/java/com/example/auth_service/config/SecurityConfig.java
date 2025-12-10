package com.example.auth_service.config;

import com.example.auth_service.domain.model.Authority;
import com.example.auth_service.security.JWTAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JWTAuthenticationFilter jwtFilter
    ) throws Exception {

        http
            // CSRF deshabilitado (API REST + Postman)
            .csrf(csrf -> csrf.disable())

            // Deshabilitamos auth legacy
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())

            // Stateless (JWT-ready)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Autorizaciones
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )

            // JWT Filter (NO bloquea si no hay token)
            .addFilterBefore(
                    jwtFilter,
                    UsernamePasswordAuthenticationFilter.class
                )

            // Necesario para H2 Console (iframes)
            .headers(headers ->
                headers.frameOptions(frame -> frame.disable())
            );

        return http.build();
    }
}
