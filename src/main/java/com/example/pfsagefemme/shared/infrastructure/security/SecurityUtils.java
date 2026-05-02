package com.example.pfsagefemme.shared.infrastructure.security;

import com.example.pfsagefemme.shared.domain.enums.RoleEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

/** Helpers pour lire l'utilisateur courant depuis le SecurityContext. */
public final class SecurityUtils {

    private SecurityUtils() {}

    public static Optional<UUID> currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof UserDetails ud)) {
            return Optional.empty();
        }
        return Optional.of(UUID.fromString(ud.getUsername()));
    }

    public static UUID requireCurrentUserId() {
        return currentUserId().orElseThrow(() -> new IllegalStateException("No authenticated user in security context"));
    }

    public static Optional<RoleEnum> currentRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return Optional.empty();
        return auth.getAuthorities().stream()
                .findFirst()
                .map(g -> g.getAuthority().replace("ROLE_", ""))
                .map(RoleEnum::valueOf);
    }
}
