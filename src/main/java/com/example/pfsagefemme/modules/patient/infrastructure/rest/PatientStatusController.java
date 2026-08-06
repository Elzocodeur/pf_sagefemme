package com.example.pfsagefemme.modules.patient.infrastructure.rest;

import com.example.pfsagefemme.modules.patient.domain.port.in.GetPatientStatusDashboardUseCase;
import com.example.pfsagefemme.modules.patient.domain.port.in.ListPatientsByStatusUseCase;
import com.example.pfsagefemme.modules.patient.domain.port.in.UpdatePatientStatusUseCase;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.ArretPatienteRequest;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.PatientStatusRowResponse;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.PatientStatusStatsResponse;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.ReprisePatienteRequest;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.mapper.PatientStatusRowMapper;
import com.example.pfsagefemme.shared.infrastructure.web.ApiPaths;
import com.example.pfsagefemme.shared.infrastructure.web.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "9 - Statuts patientes", description = "Dashboard arretees / abandons + transitions de cycle de vie")
@RestController
@RequestMapping(ApiPaths.API_V1)
@RequiredArgsConstructor
public class PatientStatusController {

    private final GetPatientStatusDashboardUseCase dashboardUseCase;
    private final ListPatientsByStatusUseCase listByStatusUseCase;
    private final UpdatePatientStatusUseCase updateStatusUseCase;

    @Operation(summary = "Compteurs de l'ecran 'Statuts patientes'")
    @PreAuthorize("hasAnyRole('SAGE_FEMME', 'ADMIN_POSTE', 'SUPER_ADMIN')")
    @GetMapping("/statuts/stats")
    public PatientStatusStatsResponse stats(
            @Parameter(description = "Filtre par poste (null = global)")
            @RequestParam(required = false) UUID posteSanteId
    ) {
        var s = dashboardUseCase.stats(posteSanteId);
        return new PatientStatusStatsResponse(s.arretees(), s.arreteesCeMois(), s.abandons(), s.anciennesReprises());
    }

    @Operation(summary = "Liste paginee des patientes arretees")
    @PreAuthorize("hasAnyRole('SAGE_FEMME', 'ADMIN_POSTE', 'SUPER_ADMIN')")
    @GetMapping("/statuts/arretees")
    public PageResponse<PatientStatusRowResponse> arretees(
            @RequestParam(required = false) UUID posteSanteId,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return PageResponse.of(listByStatusUseCase.arretees(posteSanteId, q, page, size)
                .map(PatientStatusRowMapper::toResponse));
    }

    @Operation(
            summary = "Liste paginee des patientes en abandon (sans visite depuis N jours)",
            description = "Calcul dynamique : patientes ACTIVE sans visite depuis {thresholdDays} jours (defaut 90)."
    )
    @PreAuthorize("hasAnyRole('SAGE_FEMME', 'ADMIN_POSTE', 'SUPER_ADMIN')")
    @GetMapping("/statuts/abandons")
    public PageResponse<PatientStatusRowResponse> abandons(
            @RequestParam(required = false) UUID posteSanteId,
            @Parameter(description = "Seuil en jours sans visite", example = "90")
            @RequestParam(defaultValue = "90") int thresholdDays,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return PageResponse.of(listByStatusUseCase.abandons(posteSanteId, thresholdDays, q, page, size)
                .map(PatientStatusRowMapper::toResponse));
    }

    @Operation(
            summary = "Marque une patiente comme ARRETEE",
            description = "Reserve aux SAGE_FEMME. Si deja ARRETEE, renvoie la patiente sans changement (idempotent)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Patiente arretee"),
            @ApiResponse(responseCode = "404", description = "Patiente introuvable", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasRole('SAGE_FEMME')")
    @PostMapping("/patientes/{id}/arreter")
    public ResponseEntity<Void> arreter(@PathVariable UUID id, @Valid @RequestBody ArretPatienteRequest request) {
        updateStatusUseCase.arreter(new UpdatePatientStatusUseCase.ArretCommand(id, request.motif(), request.dateArret()));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Reprend une patiente arretee/abandonnee",
            description = "Bouton 'Reprendre' du dashboard. Repasse la patiente en ACTIVE."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Patiente reprise"),
            @ApiResponse(responseCode = "404", description = "Patiente introuvable", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "422", description = "Patiente deja active", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasRole('SAGE_FEMME')")
    @PostMapping("/patientes/{id}/reprendre")
    public ResponseEntity<Void> reprendre(@PathVariable UUID id, @Valid @RequestBody(required = false) ReprisePatienteRequest request) {
        var observations = request == null ? null : request.observations();
        updateStatusUseCase.reprendre(new UpdatePatientStatusUseCase.RepriseCommand(id, observations));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
