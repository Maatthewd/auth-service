package com.example.auth_service;
import com.example.auth_service.domain.model.Role;
import com.example.auth_service.domain.model.User;
import com.example.auth_service.repository.AuthRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UserLoginSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void login_withValidCredentials_shouldReturn200() throws Exception {
        authRepository.save(
                User.builder()
                        .username("user")
                        .password(passwordEncoder.encode("123456"))
                        .roles(Set.of(Role.ROLE_USER))
                        .build()
        );

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content("""
                            {
                              "username": "user",
                              "password": "123456"
                            }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void login_withInvalidCredentials_shouldReturn401() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content("""
                            {
                              "username": "user",
                              "password": "wrong"
                            }
                        """))
                .andExpect(status().isUnauthorized());
    }
}