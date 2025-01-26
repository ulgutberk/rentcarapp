package com.rentcarapp.exception;

import com.rentcarapp.model.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomExceptions.UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserAlreadyExists(CustomExceptions.UserAlreadyExistsException ex) {
        ApiResponse<Void> response = ApiResponse.of(
                false,
                ex.getMessage(),
                null,
                409  // Conflict
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(CustomExceptions.MissingParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParameter(CustomExceptions.MissingParameterException ex) {
        ApiResponse<Void> response = ApiResponse.of(
                false,
                ex.getMessage(),
                null,
                400  // Bad Request
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CustomExceptions.UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFound(CustomExceptions.UserNotFoundException ex) {
        ApiResponse<Void> response = ApiResponse.of(
                false,
                ex.getMessage(),
                null,
                404  // Not Found
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(CustomExceptions.WrongUUIDFormatException.class)
    public ResponseEntity<ApiResponse<Void>> handleWrongUUIDFormatException(CustomExceptions.WrongUUIDFormatException ex) {
        ApiResponse<Void> response = ApiResponse.of(
                false,
                ex.getMessage(),
                null,
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}