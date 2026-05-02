package com.example.pfsagefemme.modules.patient.infrastructure.rest;

import com.example.pfsagefemme.modules.patient.domain.port.in.GetPvvihDashboardUseCase;
import com.example.pfsagefemme.modules.patient.domain.port.in.ListPvvihPatientsUseCase;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.PvvihRowResponse;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.PvvihStatsResponse;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.mapper.PvvihRowMapper;
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

import java.util.List;
import java.util.UUID;

@Tag(name = "8 - PVVIH (Section 5.2.3)", description = "Tableau de bord et listing des patientes vivant avec le VIH")
@RestController
@RequestMapping(ApiPaths.API_V1 + "/pvvih")
@RequiredArgsConstructor
public class PvvihController {

    private final GetPvvihDashboardUseCase getPvvihDashboardUseCase;
    private final ListPvvihPatientsUseCase listPvvihPatientsUseCase;

    @Operation(
            summary = "Compteurs Femmes PVVIH",
            description = "Renvoie les 3 cards : cumul annuel, actives sous traitement, nouvelles ce mois."
    )
    @PreAuthorize("hasAnyRole('SAGE_FEMME', 'ADMIN_POSTE', 'SUPER_ADMIN')")
    @GetMapping("/stats")
    public PvvihStatsResponse stats(
            @Parameter(description = "Filtre par poste (null = global)")
            @RequestParam(required = false) UUID posteSanteId
    ) {
        var s = getPvvihDashboardUseCase.stats(posteSanteId);
        return new PvvihStatsResponse(s.totalAnnuel(), s.actives(), s.nouvellesCeMois());
    }

    @Operation(
            summary = "Liste des patientes PVVIH",
            description = "Vue tableau enrichi (stade OMS, ARV, methode PF, statut suivi, prochain RDV)."
    )
    @PreAuthorize("hasAnyRole('SAGE_FEMME', 'ADMIN_POSTE', 'SUPER_ADMIN')")
    @GetMapping
    public List<PvvihRowResponse> list(
            @Parameter(description = "Filtre par poste (null = global)")
            @RequestParam(required = false) UUID posteSanteId
    ) {
        return listPvvihPatientsUseCase.list(posteSanteId).stream()
                .map(PvvihRowMapper::toResponse)
                .toList();
    }
}
