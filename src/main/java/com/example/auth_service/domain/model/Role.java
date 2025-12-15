package com.example.auth_service.domain.model;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {

    ROLE_USER(Set.of(
            Authority.READ_SELF,
            Authority.UPDATE_SELF,
            Authority.DELETE_SELF
            )),

    ROLE_ADMIN(Set.of(
            Authority.ADMIN_AUTHORITY,

            Authority.READ_USERS,
            Authority.CREATE_USERS,
            Authority.UPDATE_USERS,
            Authority.DELETE_USERS,

            Authority.READ_SELF,
            Authority.UPDATE_SELF,
            Authority.DELETE_SELF
    ));

    private final Set<Authority> authorities;

    Role(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public Set<String> getGrantedAuthorities() {
        return authorities.stream()
                .map(auth -> auth.name())
                .collect(Collectors.toSet());
    }

    public String getRoleName() {
        return "ROLE_" + this.name();
    }
}
