package com.example.auth_service.exception;

import com.example.auth_service.domain.exception.*;
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

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiError> invalidTokenException (InvalidTokenException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(toApiError(
                        HttpStatus.UNAUTHORIZED,
                        "InvalidToken",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> userNotFoundException (UserNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(toApiError(
                        HttpStatus.NOT_FOUND,
                        "NotFound",
                        e.getMessage()
                ));
    }


    @ExceptionHandler(InvalidJwtTokenException.class)
    public ResponseEntity<ApiError> invalidJwtTokenException (InvalidJwtTokenException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(toApiError(
                        HttpStatus.UNAUTHORIZED,
                        "InvalidToken",
                        e.getMessage()
                ));
    }

}
