package com.example.pfsagefemme.modules.user.infrastructure.security;

import com.example.pfsagefemme.modules.user.domain.model.entity.AdminPoste;
import com.example.pfsagefemme.modules.user.domain.model.entity.SageFemme;
import com.example.pfsagefemme.modules.user.domain.model.entity.Utilisateur;
import com.example.pfsagefemme.modules.user.domain.port.in.GetCurrentUserUseCase;
import com.example.pfsagefemme.shared.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

/**
 * Cloisonnement des donnees par poste de sante.
 *
 * SAGE_FEMME et ADMIN_POSTE ne voient que leur propre poste : le poste est deduit
 * de l'utilisateur authentifie, jamais du parametre de la requete.
 * SUPER_ADMIN garde le filtre demande (null = vue globale).
 */
@Component
@RequiredArgsConstructor
public class PosteScope {

    private final GetCurrentUserUseCase getCurrentUserUseCase;

    /** Poste a appliquer au filtre. Un poste demande different du sien leve un 403. */
    public UUID resolve(UUID requestedPosteId) {
        Utilisateur u = getCurrentUserUseCase.byId(SecurityUtils.requireCurrentUserId());
        if (u instanceof SageFemme || u instanceof AdminPoste) {
            UUID own = posteOf(u);
            if (own == null) {
                throw new AccessDeniedException("Utilisateur non rattache a un poste");
            }
            if (requestedPosteId != null && !requestedPosteId.equals(own)) {
                throw new AccessDeniedException("Acces a un autre poste refuse");
            }
            return own;
        }
        return requestedPosteId;
    }

    /** Verifie qu'une ressource appartenant a {@code resourcePosteId} est visible par l'utilisateur courant. */
    public void checkAccess(UUID resourcePosteId) {
        UUID scope = resolve(null);
        if (scope != null && !Objects.equals(scope, resourcePosteId)) {
            throw new AccessDeniedException("Acces a un autre poste refuse");
        }
    }

    private static UUID posteOf(Utilisateur u) {
        if (u instanceof SageFemme sf && sf.getPosteSante() != null) return sf.getPosteSante().getId();
        if (u instanceof AdminPoste ap && ap.getPosteSante() != null) return ap.getPosteSante().getId();
        return null;
    }
}
