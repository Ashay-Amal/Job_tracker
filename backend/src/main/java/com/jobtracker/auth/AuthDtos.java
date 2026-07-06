package com.jobtracker.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public final class AuthDtos {
    private AuthDtos() {
    }

    public record RegisterRequest(
            @Email @NotBlank String email,
            @Size(min = 8, max = 128) String password,
            @NotBlank String fullName
    ) {
    }

    public record LoginRequest(
            @Email @NotBlank String email,
            @NotBlank String password
    ) {
    }

    public record RefreshRequest(@NotBlank String refreshToken) {
    }

    public record ForgotPasswordRequest(@Email @NotBlank String email) {
    }

    public record ResetPasswordRequest(
            @NotBlank String token,
            @Size(min = 8, max = 128) String newPassword
    ) {
    }

    public record UserDto(UUID id, String email, String fullName) {
    }

    public record AuthResponse(String accessToken, String refreshToken, UserDto user) {
    }

    public record MessageResponse(String message) {
    }
}
