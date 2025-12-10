package com.example.auth_service.exception;

import com.example.auth_service.domain.exception.UsernameAlreadyExistsException;
import com.example.auth_service.domain.exception.WrongCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private ApiError toApiError(HttpStatus status, String error, String message) {
        return new ApiError(status, error, message);
    }


    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiError> usernameAlreadyExistsException(UsernameAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(toApiError(
                        HttpStatus.CONFLICT,
                        "UserAlreadyExists",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(WrongCredentialsException.class)
    public ResponseEntity<ApiError> wrongPassowrdException (WrongCredentialsException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(toApiError(
                        HttpStatus.UNAUTHORIZED,
                        "InvalidCredentials",
                        e.getMessage()
                ));
    }

}
