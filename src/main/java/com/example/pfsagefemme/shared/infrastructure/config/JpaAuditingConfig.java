package com.example.pfsagefemme.shared.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Active @CreatedDate / @LastModifiedDate / @CreatedBy sur les entites
 * heritant de {@link com.example.pfsagefemme.shared.infrastructure.persistence.BaseJpaEntity}.
 * Le bean {@code AuditorAware<String>} est fourni par {@link AuditorAwareConfig}.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaAuditingConfig {
}
