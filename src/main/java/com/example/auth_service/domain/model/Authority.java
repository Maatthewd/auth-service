package com.example.auth_service.domain.model;

public enum Authority{
    ADMIN_AUTHORITY,

    READ_USERS,
    CREATE_USERS,
    UPDATE_USERS,
    DELETE_USERS,

    READ_SELF,
    UPDATE_SELF,
    DELETE_SELF
}