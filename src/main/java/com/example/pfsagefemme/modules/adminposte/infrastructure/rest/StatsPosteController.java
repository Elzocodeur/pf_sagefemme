package com.example.pfsagefemme.modules.adminposte.infrastructure.rest;

import com.example.pfsagefemme.modules.adminposte.domain.port.in.GetStatsPosteUseCase;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto.StatsPosteResponse;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.mapper.ChefPosteWebMapper;
import com.example.pfsagefemme.modules.user.domain.model.entity.AdminPoste;
import com.example.pfsagefemme.modules.user.domain.port.in.GetCurrentUserUseCase;
import com.example.pfsagefemme.shared.infrastructure.security.SecurityUtils;
import com.example.pfsagefemme.shared.infrastructure.web.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "7 - Chef de poste : Statistiques poste (ADMIN_POSTE)",
        description = "Ecran 'Statistiques poste / Vue generale' - KPIs + actives recentes + a surveiller + repartition methodes")
@RestController
@RequestMapping(ApiPaths.API_V1 + "/chef-poste/stats")
@RequiredArgsConstructor
public class StatsPosteController {

    private final GetStatsPosteUseCase useCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;

    @Operation(
            summary = "Vue generale du poste - KPIs consolides et alertes",
            description = """
                    Retourne en une seule reponse :
                      - 5 KPI cards : Total / Actives / Arretees / Abandons / PVVIH
                      - Actives recentes (top 5) avec methode contraceptive
                      - A surveiller : abandons > 90j + arretees recentes
                      - Repartition par categorie de methode
                      - Performance : taux de retention, methode la plus sollicitee, evolution mensuelle
                    """
    )
    @ApiResponse(responseCode = "200", description = "OK")
    @PreAuthorize("hasRole('ADMIN_POSTE')")
    @GetMapping
    public StatsPosteResponse load() {
        return ChefPosteWebMapper.toResponse(useCase.load(requirePosteId()));
    }

    private UUID requirePosteId() {
        var u = getCurrentUserUseCase.byId(SecurityUtils.requireCurrentUserId());
        if (u instanceof AdminPoste ap && ap.getPosteSante() != null) {
            return ap.getPosteSante().getId();
        }
        throw new IllegalStateException("Admin poste sans poste rattaché");
    }
}
