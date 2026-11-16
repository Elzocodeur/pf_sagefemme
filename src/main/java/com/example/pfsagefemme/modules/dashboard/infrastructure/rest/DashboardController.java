package com.example.pfsagefemme.modules.dashboard.infrastructure.rest;

import com.example.pfsagefemme.modules.dashboard.domain.port.in.GetSageFemmeDashboardUseCase;
import com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto.SageFemmeDashboardResponse;
import com.example.pfsagefemme.modules.dashboard.infrastructure.rest.mapper.DashboardWebMapper;
import com.example.pfsagefemme.modules.user.infrastructure.security.PosteScope;
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
    private final PosteScope posteScope;

    @Operation(
            summary = "Dashboard sage-femme",
            description = """
                    Retourne en une seule reponse l'integralite du dashboard :
                      - 5 cards KPI (Total / Actives / Arretees / Abandons / PVVIH) avec deltas mensuels
                      - histogramme d'activite (lundi -> dimanche de la semaine en cours)
                      - bloc 'Abandons a relancer' (top 3 RdV manques > 15j)
                      - tableau 'Actives recentes' (top 3 par derniere visite)

                    Le poste est impose par l'utilisateur connecte (SAGE_FEMME / ADMIN_POSTE) ; seul un SUPER_ADMIN peut filtrer librement.
                    """
    )
    @PreAuthorize("hasAnyRole('SAGE_FEMME', 'ADMIN_POSTE', 'SUPER_ADMIN')")
    @GetMapping
    public SageFemmeDashboardResponse load(
            @Parameter(description = "Filtre par poste (defaut : poste de l'utilisateur courant)")
            @RequestParam(required = false) UUID posteSanteId
    ) {
        UUID effectivePosteId = posteScope.resolve(posteSanteId);
        return DashboardWebMapper.toResponse(dashboardUseCase.load(effectivePosteId));
    }

}
