package com.example.pfsagefemme.modules.consultation.infrastructure.rest;

import com.example.pfsagefemme.modules.consultation.domain.port.in.CreateVisiteUseCase;
import com.example.pfsagefemme.modules.consultation.domain.port.in.ListVisitesByPatienteUseCase;
import com.example.pfsagefemme.modules.consultation.domain.port.in.ValidateVisiteUseCase;
import com.example.pfsagefemme.modules.consultation.infrastructure.rest.dto.CreateVisiteRequest;
import com.example.pfsagefemme.modules.consultation.infrastructure.rest.dto.VisiteDetailResponse;
import com.example.pfsagefemme.modules.consultation.infrastructure.rest.mapper.VisiteWebMapper;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.PatienteAccessGuard;
import com.example.pfsagefemme.shared.infrastructure.security.SecurityUtils;
import com.example.pfsagefemme.shared.infrastructure.web.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "7 - Visites de suivi (SAGE_FEMME)", description = "Enregistrement des visites cliniques de suivi des patientes")
@RestController
@RequestMapping(ApiPaths.API_V1)
@RequiredArgsConstructor
public class VisiteController {

    private final CreateVisiteUseCase createVisiteUseCase;
    private final ListVisitesByPatienteUseCase listVisitesByPatienteUseCase;
    private final ValidateVisiteUseCase validateVisiteUseCase;
    private final PatienteAccessGuard patienteAccessGuard;

    @Operation(
            summary = "Enregistre une nouvelle visite de suivi",
            description = """
                    Cree une visite (constantes + observations) pour une patiente existante.

                    Si le bloc {@code methodChange} est fourni, la visite acte EN PLUS un changement
                    de methode contraceptive : un {@code ChangementMethode} est cree et la methode
                    courante de la fiche de consultation est mise a jour. Tout est atomique.

                    Le champ {@code statut} accepte BROUILLON ou VALIDEE (defaut : VALIDEE).
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Visite creee"),
            @ApiResponse(responseCode = "404", description = "Patiente introuvable", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "422", description = "Changement de methode invalide (pas de methode courante, ou identique)", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasRole('SAGE_FEMME')")
    @PostMapping("/patientes/{patienteId}/visites")
    public ResponseEntity<VisiteDetailResponse> create(
            @PathVariable UUID patienteId,
            @Valid @RequestBody CreateVisiteRequest request
    ) {
        patienteAccessGuard.check(patienteId);
        var visite = createVisiteUseCase.create(
                VisiteWebMapper.toCommand(patienteId, SecurityUtils.requireCurrentUserId(), request)
        );
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ApiPaths.API_V1 + "/visites/{id}").buildAndExpand(visite.getId()).toUri();
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(VisiteWebMapper.toResponse(visite));
    }

    @Operation(summary = "Liste les visites d'une patiente (de la plus recente a la plus ancienne)")
    @PreAuthorize("hasAnyRole('SAGE_FEMME', 'ADMIN_POSTE', 'SUPER_ADMIN')")
    @GetMapping("/patientes/{patienteId}/visites")
    public List<VisiteDetailResponse> listByPatiente(@PathVariable UUID patienteId) {
        patienteAccessGuard.check(patienteId);
        return listVisitesByPatienteUseCase.list(patienteId).stream()
                .map(VisiteWebMapper::toResponse)
                .toList();
    }

    @Operation(summary = "Valide une visite en brouillon", description = "Idempotent : appel sur une visite deja validee renvoie la visite sans changement.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Visite validee"),
            @ApiResponse(responseCode = "404", description = "Visite introuvable", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasRole('SAGE_FEMME')")
    @PostMapping("/visites/{id}/validate")
    public VisiteDetailResponse validate(@PathVariable UUID id) {
        return VisiteWebMapper.toResponse(validateVisiteUseCase.validate(id));
    }
}
