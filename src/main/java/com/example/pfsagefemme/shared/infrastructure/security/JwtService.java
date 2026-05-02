package com.example.pfsagefemme.shared.infrastructure.security;

import com.example.pfsagefemme.shared.domain.enums.RoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * Generation et validation des JSON Web Tokens.
 * Le secret est lu depuis {@link JwtProperties} (variable d'env JWT_SECRET).
 */
@Service
public class JwtService {

    private static final String CLAIM_ROLE = "role";
    private static final String CLAIM_EMAIL = "email";

    private final JwtProperties properties;
    private final SecretKey key;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
        byte[] decoded = Base64.getDecoder().decode(properties.secret());
        this.key = Keys.hmacShaKeyFor(decoded);
    }

    public String generate(UUID userId, String email, RoleEnum role) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(properties.expirationMinutes(), ChronoUnit.MINUTES);
        return Jwts.builder()
                .issuer(properties.issuer())
                .subject(userId.toString())
                .claim(CLAIM_EMAIL, email)
                .claim(CLAIM_ROLE, role.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(key)
                .compact();
    }

    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(parse(token).getSubject());
    }

    public long expirationSeconds() {
        return properties.expirationMinutes() * 60;
    }
}
