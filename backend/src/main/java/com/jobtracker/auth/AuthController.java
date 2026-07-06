package com.jobtracker.auth;

import com.jobtracker.auth.AuthDtos.AuthResponse;
import com.jobtracker.auth.AuthDtos.ForgotPasswordRequest;
import com.jobtracker.auth.AuthDtos.LoginRequest;
import com.jobtracker.auth.AuthDtos.MessageResponse;
import com.jobtracker.auth.AuthDtos.RefreshRequest;
import com.jobtracker.auth.AuthDtos.RegisterRequest;
import com.jobtracker.auth.AuthDtos.ResetPasswordRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final CurrentUser currentUser;

    public AuthController(AuthService authService, CurrentUser currentUser) {
        this.authService = authService;
        this.currentUser = currentUser;
    }

    @PostMapping("/register")
    AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    AuthResponse refresh(@Valid @RequestBody RefreshRequest request) {
        return authService.refresh(request.refreshToken());
    }

    @PostMapping("/forgot-password")
    MessageResponse forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request.email());
        return new MessageResponse("If the account exists, a reset link has been sent.");
    }

    @PostMapping("/reset-password")
    MessageResponse resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.token(), request.newPassword());
        return new MessageResponse("Password reset complete.");
    }

    @GetMapping("/me")
    AuthDtos.UserDto me() {
        var user = currentUser.get();
        return new AuthDtos.UserDto(user.getId(), user.getEmail(), user.getFullName());
    }
}
