package com.example.auth_service;

import com.example.auth_service.domain.model.Role;
import com.example.auth_service.domain.model.User;
import com.example.auth_service.repository.AuthRepository;
import com.example.auth_service.service.impl.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
class UserRefreshTokenSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthRepository authRepository;

    @Test
    void refresh_withValidRefreshToken_shouldReturn200() throws Exception {
        User user = authRepository.save(
                User.builder()
                        .username("user")
                        .password("123456")
                        .roles(Set.of(Role.ROLE_USER))
                        .enabled(true)
                        .build());

        String refreshToken = jwtService.generateRefreshToken(user).refreshToken();
        user.setRefreshToken(refreshToken);

        authRepository.save(user);


        String requestJson = """
                {
                "userToken": "%s"
                }
                """.formatted(refreshToken);

        mockMvc.perform(post("/auth/refresh-token")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void refresh_withAccessToken_shouldReturn401() throws Exception {
        User user = authRepository.save(
                User.builder()
                        .username("user")
                        .password("123456")
                        .roles(Set.of(Role.ROLE_USER))
                        .build()
        );

        String accessToken = jwtService.generateAccessToken(user).accessToken();

        mockMvc.perform(post("/auth/refresh-token")
                        .contentType("application/json")
                        .content("""
                                {
                                "userToken": "%s"
                                }
                                """.formatted(accessToken)))
                .andExpect(status().isUnauthorized());
    }
}

