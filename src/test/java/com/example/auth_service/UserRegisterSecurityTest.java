package com.example.auth_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
class UserRegisterSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void register_withValidData_shouldReturn201() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content("""
                            {
                              "username": "newUser",
                              "password": "123456"
                            }
                        """))
                .andExpect(status().isCreated());
    }

    @Test
    void register_withDuplicateUsername_shouldReturn409() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content("""
                            {
                              "username": "user",
                              "password": "123456"
                            }
                        """));

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content("""
                            {
                              "username": "user",
                              "password": "123456"
                            }
                        """))
                .andExpect(status().isConflict());
    }
}

