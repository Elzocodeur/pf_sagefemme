package com.example.pfsagefemme.modules.user.domain.port.out;

/**
 * Port sortant - hash et verification de mot de passe.
 * Garde le domaine ignorant de BCrypt / Spring Security.
 */
public interface PasswordEncoderPort {
    String hash(String rawPassword);
    boolean matches(String rawPassword, String hashedPassword);
}
