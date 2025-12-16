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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
class UserMeSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthRepository authRepository;

    @Test
    void me_withValidAccessToken_shouldReturn200() throws Exception {
        User user = authRepository.save(
                User.builder()
                        .username("user")
                        .password("123456")
                        .roles(Set.of(Role.ROLE_USER))
                        .build()
        );

        String token = jwtService.generateAccessToken(user).accessToken();

        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void me_withoutToken_shouldReturn401() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }
}
