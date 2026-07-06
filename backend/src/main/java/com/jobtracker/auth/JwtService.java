package com.jobtracker.auth;

import com.jobtracker.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final AppProperties properties;
    private final SecretKey key;

    public JwtService(AppProperties properties) {
        this.properties = properties;
        byte[] raw = properties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8);
        if (raw.length < 32) {
            raw = Arrays.copyOf(raw, 32);
        }
        this.key = Keys.hmacShaKeyFor(raw);
    }

    public String accessToken(String email) {
        return token(email, "access", Instant.now().plusSeconds(properties.getJwt().getAccessTokenMinutes() * 60));
    }

    public String refreshToken(String email) {
        return token(email, "refresh", Instant.now().plusSeconds(properties.getJwt().getRefreshTokenDays() * 86_400));
    }

    public Claims parse(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    public String subject(String token) {
        return parse(token).getSubject();
    }

    public boolean isType(String token, String type) {
        return type.equals(parse(token).get("typ", String.class));
    }

    private String token(String email, String type, Instant expiresAt) {
        return Jwts.builder()
                .subject(email)
                .claim("typ", type)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(expiresAt))
                .signWith(key)
                .compact();
    }
}
