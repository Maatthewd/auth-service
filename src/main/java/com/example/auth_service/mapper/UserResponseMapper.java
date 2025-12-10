package com.example.auth_service.mapper;

import com.example.auth_service.domain.model.User;
import com.example.auth_service.dto.response.UserResponse;
import org.springframework.stereotype.Component;

import java.sql.SQLOutput;

@Component
public class UserResponseMapper {

    public UserResponse toUserResponse(User user) {

        return new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getRoles(),
                        user.isEnabled(),
                        user.getCreatedAt(),
                        user.getUpdatedAt()
                );


    }
}
