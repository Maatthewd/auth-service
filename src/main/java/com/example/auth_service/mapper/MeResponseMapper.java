package com.example.auth_service.mapper;

import com.example.auth_service.domain.model.Authority;
import com.example.auth_service.domain.model.User;
import com.example.auth_service.dto.response.MeResponse;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class MeResponseMapper {

    public MeResponse toMeResponse(User user) {

        if(user==null) {
            return null;
        }

        return new MeResponse(
                user.getUsername(),
                new HashSet<>(user.getRoles()),
                user.getAuthorities().
                        stream()
                        .map(a -> Authority.valueOf(a.getAuthority()))
                        .collect(Collectors.toSet())
        );
    }

}
