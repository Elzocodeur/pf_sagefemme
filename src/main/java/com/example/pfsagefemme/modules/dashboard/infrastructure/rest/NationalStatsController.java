package com.example.pfsagefemme.modules.dashboard.infrastructure.rest;

import com.example.pfsagefemme.modules.dashboard.domain.port.in.GetNationalStatsUseCase;
import com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto.NationalStatsResponse;
import com.example.pfsagefemme.modules.dashboard.infrastructure.rest.mapper.NationalStatsWebMapper;
import com.example.pfsagefemme.shared.infrastructure.web.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "13 - Statistiques nationales (SUPER_ADMIN)",
        description = "Vue agregee : 5 cards globales + tableau par poste + dernieres patientes")
@RestController
@RequestMapping(ApiPaths.API_V1 + "/stats/national")
@RequiredArgsConstructor
public class NationalStatsController {

    private final GetNationalStatsUseCase useCase;

    @Operation(
            summary = "Statistiques nationales",
            description = """
                    Retourne en une seule reponse :
                      - 5 cards globales (Total Patientes / Actives / Abandons / PVVIH / Sages-femmes)
                      - tableau par poste avec compteurs et taux d'activite
                      - total national + taux moyen
                      - les 10 dernieres patientes enregistrees (toutes regions)
                    """
    )
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping
    public NationalStatsResponse load() {
        return NationalStatsWebMapper.toResponse(useCase.load());
    }
}
