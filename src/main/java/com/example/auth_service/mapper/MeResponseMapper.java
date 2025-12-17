package com.example.auth_service.mapper;


import com.example.auth_service.domain.model.User;
import com.example.auth_service.dto.response.MeResponse;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MeResponseMapper {

    public MeResponse toMeResponse(User user) {

        if(user==null) {
            return null;
        }

        Set<String> authorities = user.getRoles().stream()
            .flatMap(role -> role.getGrantedAuthorities().stream())
            .collect(Collectors.toSet());
        
        return new MeResponse(
                user.getUsername(),
                new HashSet<>(user.getRoles()),
                authorities
        );
    }

}
