package com.example.pfsagefemme.shared.infrastructure.config;

import com.example.pfsagefemme.shared.infrastructure.security.SecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * Resolveur de l'utilisateur courant pour le champ {@code createdBy}.
 *
 * Lit l'id de l'utilisateur depuis le SecurityContext (via {@link SecurityUtils}).
 * Pour les operations sans contexte (bootstrap, batch), retombe sur "system".
 */
@Configuration
public class AuditorAwareConfig {

    private static final String SYSTEM = "system";

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of(
                SecurityUtils.currentUserId().map(Object::toString).orElse(SYSTEM)
        );
    }
}
