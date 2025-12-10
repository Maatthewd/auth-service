package com.example.auth_service.domain.model;

import java.util.Set;

public enum Role {

    USER(Set.of(Authority.READ_SELF)),

    ADMIN(Set.of(
            Authority.READ_USERS,
            Authority.CREATE_USERS,
            Authority.UPDATE_USERS,
            Authority.DELETE_USERS
    ));

    private final Set<Authority> authorities;

    Role(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }
}
