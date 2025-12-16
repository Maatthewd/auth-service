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
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class UserCreateSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthRepository authRepository;

    private User createAdmin() {
        return authRepository.save(
                User.builder()
                        .username("admin_" + UUID.randomUUID())
                        .password("123456")
                        .roles(Set.of(Role.ROLE_ADMIN))
                        .build()
        );
    }

    @Test
    void createUser_withAdminToken_shouldReturn201() throws Exception {
        String token = jwtService.generateAccessToken(createAdmin()).accessToken();

        mockMvc.perform(post("/users")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content("""
                            {
                              "username": "userTest",
                              "password": "123456"
                            }
                        """))
                .andExpect(status().isCreated());
    }

    @Test
    void createUser_withoutToken_shouldReturn401() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }
}
