package com.example.auth_service.exception;

import com.example.auth_service.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ApiError toApiError(HttpStatus status, String code, String message) {
        return new ApiError(status, code, message);
    }

    // 409 - El username ya existe
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleUsernameAlreadyExists(UsernameAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(toApiError(
                        HttpStatus.CONFLICT,
                        "USERNAME_ALREADY_EXISTS",
                        e.getMessage()
                ));
    }

    // 401 - Credenciales incorrectas
    @ExceptionHandler(WrongCredentialsException.class)
    public ResponseEntity<ApiError> handleWrongCredentials(WrongCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(toApiError(
                        HttpStatus.UNAUTHORIZED,
                        "INVALID_CREDENTIALS",
                        e.getMessage()
                ));
    }

    // 401 - Token inválido funcionalmente (no autorizado)
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiError> handleInvalidToken(InvalidTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(toApiError(
                        HttpStatus.UNAUTHORIZED,
                        "INVALID_TOKEN",
                        e.getMessage()
                ));
    }

    // 401 - Error técnico del JWT (expirado, firma inválida, etc.)
    @ExceptionHandler(InvalidJwtTokenException.class)
    public ResponseEntity<ApiError> handleInvalidJwtToken(InvalidJwtTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(toApiError(
                        HttpStatus.UNAUTHORIZED,
                        "JWT_ERROR",
                        e.getMessage()
                ));
    }

    // 404 - Usuario no encontrado
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(toApiError(
                        HttpStatus.NOT_FOUND,
                        "USER_NOT_FOUND",
                        e.getMessage()
                ));
    }

}
