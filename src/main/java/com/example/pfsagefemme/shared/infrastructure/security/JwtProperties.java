package com.example.pfsagefemme.shared.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Proprietes de configuration JWT.
 * Charge depuis application.properties prefixe app.security.jwt.*
 */
@ConfigurationProperties(prefix = "app.security.jwt")
public record JwtProperties(
        String secret,
        long expirationMinutes,
        String issuer
) {}
