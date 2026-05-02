package com.example.pfsagefemme.modules.dashboard.infrastructure.rest;

import com.example.pfsagefemme.modules.dashboard.domain.port.in.GetSageFemmeDashboardUseCase;
import com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto.SageFemmeDashboardResponse;
import com.example.pfsagefemme.modules.dashboard.infrastructure.rest.mapper.DashboardWebMapper;
import com.example.pfsagefemme.modules.user.domain.model.entity.SageFemme;
import com.example.pfsagefemme.modules.user.domain.port.in.GetCurrentUserUseCase;
import com.example.pfsagefemme.shared.infrastructure.security.SecurityUtils;
import com.example.pfsagefemme.shared.infrastructure.web.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "12 - Dashboard sage-femme", description = "Vue d'ensemble : KPIs, activite 7 jours, abandons a relancer, actives recentes")
@RestController
@RequestMapping(ApiPaths.API_V1 + "/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final GetSageFemmeDashboardUseCase dashboardUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;

    @Operation(
            summary = "Dashboard sage-femme",
            description = """
                    Retourne en une seule reponse l'integralite du dashboard :
                      - 5 cards KPI (Total / Actives / Arretees / Abandons / PVVIH) avec deltas mensuels
                      - histogramme d'activite (lundi -> dimanche de la semaine en cours)
                      - bloc 'Abandons a relancer' (top 3 RdV manques > 15j)
                      - tableau 'Actives recentes' (top 3 par derniere visite)

                    Le filtre `posteSanteId` est optionnel - sans valeur, on prend le poste de la sage-femme connectee.
                    """
    )
    @PreAuthorize("hasAnyRole('SAGE_FEMME', 'ADMIN_POSTE', 'SUPER_ADMIN')")
    @GetMapping
    public SageFemmeDashboardResponse load(
            @Parameter(description = "Filtre par poste (defaut : poste de l'utilisateur courant)")
            @RequestParam(required = false) UUID posteSanteId
    ) {
        UUID effectivePosteId = posteSanteId != null ? posteSanteId : currentUserPosteId();
        return DashboardWebMapper.toResponse(dashboardUseCase.load(effectivePosteId));
    }

    private UUID currentUserPosteId() {
        var u = getCurrentUserUseCase.byId(SecurityUtils.requireCurrentUserId());
        if (u instanceof SageFemme sf && sf.getPosteSante() != null) {
            return sf.getPosteSante().getId();
        }
        return null; // SUPER_ADMIN ou ADMIN sans poste -> vue globale
    }
}
