package com.example.pfsagefemme.modules.rapport.infrastructure.rest;

import com.example.pfsagefemme.modules.rapport.domain.port.in.GetRapportNationalUseCase;
import com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto.RapportNationalResponse;
import com.example.pfsagefemme.modules.rapport.infrastructure.rest.mapper.RapportNationalWebMapper;
import com.example.pfsagefemme.shared.infrastructure.web.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "14 - Rapport National PF (SUPER_ADMIN)",
        description = "Rapport National PF - agregat tous postes pour un mois donne (Section 5.2 + LARC + Hormonaux)")
@RestController
@RequestMapping(ApiPaths.API_V1 + "/rapports/national")
@RequiredArgsConstructor
public class RapportNationalController {

    private final GetRapportNationalUseCase useCase;

    @Operation(
            summary = "Rapport National PF pour un mois",
            description = """
                    Retourne :
                      - 5 cards globales (Nvelles PV avec delta % / Anc. PV / Actives / Inactives / PVVIH actives)
                      - Section 5.2 : tableau recapitulatif SNSR par district/poste
                      - Detail des Methodes de Longue Duree (LARC) : DIU + Implants
                      - Consommation des Intrants Hormonaux : Pilules + Injectables
                    """
    )
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping
    public RapportNationalResponse load(
            @Parameter(description = "Mois (1-12)") @RequestParam @Min(1) @Max(12) int mois,
            @Parameter(description = "Annee") @RequestParam @Min(2000) @Max(2100) int annee
    ) {
        return RapportNationalWebMapper.toResponse(useCase.load(mois, annee));
    }
}
