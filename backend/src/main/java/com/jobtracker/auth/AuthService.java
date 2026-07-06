package com.jobtracker.auth;

import com.jobtracker.auth.AuthDtos.AuthResponse;
import com.jobtracker.auth.AuthDtos.LoginRequest;
import com.jobtracker.auth.AuthDtos.RegisterRequest;
import com.jobtracker.auth.AuthDtos.UserDto;
import com.jobtracker.config.AppProperties;
import com.jobtracker.users.AuthProvider;
import com.jobtracker.users.UserAccount;
import com.jobtracker.users.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final MailService mailService;
    private final AppProperties appProperties;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            RefreshTokenRepository refreshTokenRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            MailService mailService,
            AppProperties appProperties
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.mailService = mailService;
        this.appProperties = appProperties;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.email().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already registered");
        }
        UserAccount user = new UserAccount();
        user.setEmail(email);
        user.setFullName(request.fullName());
        user.setProvider(AuthProvider.LOCAL);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        userRepository.save(user);
        return issueTokens(user);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.email().toLowerCase(),
                request.password()));
        UserAccount user = userRepository.findByEmail(request.email().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        return issueTokens(user);
    }

    @Transactional
    public AuthResponse refresh(String refreshToken) {
        if (!jwtService.isType(refreshToken, "refresh")) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        Claims claims = jwtService.parse(refreshToken);
        RefreshToken stored = refreshTokenRepository.findByTokenHash(TokenHash.sha256(refreshToken))
                .orElseThrow(() -> new IllegalArgumentException("Refresh token was not issued by this server"));
        if (!stored.usable()) {
            throw new IllegalArgumentException("Refresh token has expired or was revoked");
        }
        stored.revoke();
        UserAccount user = userRepository.findByEmail(claims.getSubject())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return issueTokens(user);
    }

    @Transactional
    public void forgotPassword(String email) {
        Optional<UserAccount> user = userRepository.findByEmail(email.toLowerCase());
        if (user.isEmpty()) {
            return;
        }
        String token = UUID.randomUUID().toString() + UUID.randomUUID();
        passwordResetTokenRepository.save(new PasswordResetToken(
                user.get(),
                TokenHash.sha256(token),
                Instant.now().plusSeconds(60 * 30)));
        String resetLink = appProperties.getFrontendBaseUrl() + "/reset-password?token=" + token;
        mailService.sendPasswordReset(user.get().getEmail(), resetLink);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken stored = passwordResetTokenRepository.findByTokenHash(TokenHash.sha256(token))
                .orElseThrow(() -> new IllegalArgumentException("Reset token is invalid"));
        if (!stored.usable()) {
            throw new IllegalArgumentException("Reset token has expired or was already used");
        }
        UserAccount user = stored.getUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        stored.markUsed();
    }

    @Transactional
    public AuthResponse issueTokens(UserAccount user) {
        String accessToken = jwtService.accessToken(user.getEmail());
        String refreshToken = jwtService.refreshToken(user.getEmail());
        Instant expiresAt = Instant.now().plusSeconds(appProperties.getJwt().getRefreshTokenDays() * 86_400);
        refreshTokenRepository.save(new RefreshToken(user, TokenHash.sha256(refreshToken), expiresAt));
        return new AuthResponse(accessToken, refreshToken, new UserDto(user.getId(), user.getEmail(), user.getFullName()));
    }
}
