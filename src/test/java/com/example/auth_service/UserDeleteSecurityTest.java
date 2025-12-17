package com.example.auth_service;


import com.example.auth_service.domain.exception.InvalidJwtTokenException;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class UserDeleteSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthRepository authRepository;

    private User createUser() {
        return authRepository.save(
                User.builder()
                        .username("userTest")
                        .password("123")
                        .roles(Set.of(Role.ROLE_USER))
                        .enabled(true)
                        .build()
        );
    }

    private User createAdmin() {
        return authRepository.save(
                User.builder()
                        .username("adminTest")
                        .password("123")
                        .roles(Set.of(Role.ROLE_ADMIN))
                        .enabled(true)
                        .build()
        );
    }

    @Test
    void deleteSelf_withValidAccessToken_shouldReturn204() throws Exception {
        User user = createUser();

        String accessToken = jwtService.generateAccessToken(user).accessToken();

        mockMvc.perform(delete("/users/me")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNoContent());

    }

    @Test
    void deleteUser_withValidAccessToken_shouldReturn204() throws Exception {

        User user = createUser();
        User admin = createAdmin();

        String accessToken = jwtService.generateAccessToken(admin).accessToken();

        mockMvc.perform(delete("/users/{id}", user.getId())
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNoContent());

    }

    @Test
    void deleteUser_withUserAccessToken_shouldReturn403() throws Exception {
        User user = createUser();
        User admin = createAdmin();

        String userToken = jwtService.generateAccessToken(user).accessToken();

        mockMvc.perform(delete("/users/{id}", admin.getId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUser_withRefreshToken_shouldReturn401() throws Exception {
        User user = createUser();
        User admin = createAdmin();

        String refreshToken = jwtService.generateRefreshToken(user).refreshToken();

        mockMvc.perform(delete("/users/{id}", admin.getId())
                        .header("Authorization", "Bearer " + refreshToken))
                .andExpect(status().isUnauthorized());
    }

}
