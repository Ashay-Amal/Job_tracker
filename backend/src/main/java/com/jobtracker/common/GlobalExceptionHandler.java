package com.jobtracker.common;

import jakarta.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex) {
        Map<String, String> fields = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a, b) -> a));
        return ResponseEntity.badRequest().body(new ApiError(
                java.time.Instant.now(),
                400,
                "Validation failed",
                "Please fix the highlighted fields.",
                fields));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<ApiError> notFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiError.of(404, "Not found", ex.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, BadCredentialsException.class})
    ResponseEntity<ApiError> badRequest(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ApiError.of(400, "Bad request", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiError> generic(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(500, "Server error", ex.getMessage()));
    }
}
