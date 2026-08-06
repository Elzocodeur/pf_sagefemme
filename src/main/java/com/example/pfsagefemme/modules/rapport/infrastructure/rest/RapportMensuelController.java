package com.example.pfsagefemme.modules.rapport.infrastructure.rest;

import com.example.pfsagefemme.modules.rapport.domain.model.entity.RapportMensuel;
import com.example.pfsagefemme.modules.rapport.domain.port.in.GenerateRapportMensuelUseCase;
import com.example.pfsagefemme.modules.rapport.domain.port.in.GetRapportMensuelUseCase;
import com.example.pfsagefemme.modules.rapport.domain.port.in.SubmitRapportMensuelUseCase;
import com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto.RapportMensuelResponse;
import com.example.pfsagefemme.modules.rapport.infrastructure.rest.mapper.RapportMensuelWebMapper;
import com.example.pfsagefemme.shared.infrastructure.security.SecurityUtils;
import com.example.pfsagefemme.shared.infrastructure.web.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Tag(name = "11 - Rapport mensuel PF", description = "Generation, lecture et soumission au district du rapport mensuel PF (sections 5.2.1 a 5.2.4 + matrice changement)")
@RestController
@RequestMapping(ApiPaths.API_V1 + "/rapports")
@RequiredArgsConstructor
public class RapportMensuelController {

    private final GenerateRapportMensuelUseCase generateUseCase;
    private final GetRapportMensuelUseCase getUseCase;
    private final SubmitRapportMensuelUseCase submitUseCase;

    @Operation(
            summary = "Genere le rapport mensuel pour la periode (idempotent sur BROUILLON)",
            description = """
                    Cree un nouveau rapport BROUILLON ou regenere l'existant si encore BROUILLON.
                    Si le rapport est deja VALIDE ou SOUMIS, la regeneration est refusee.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Rapport genere"),
            @ApiResponse(responseCode = "404", description = "Sage-femme ou poste introuvable", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "422", description = "Rapport deja valide / soumis", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @Transactional
    @PreAuthorize("hasRole('SAGE_FEMME')")
    @PostMapping("/generate")
    public ResponseEntity<RapportMensuelResponse> generate(
            @Parameter(description = "Mois (1-12)") @RequestParam @Min(1) @Max(12) int mois,
            @Parameter(description = "Annee") @RequestParam @Min(2000) @Max(2100) int annee,
            @Parameter(description = "Poste de sante (defaut : poste de la sage-femme connectee)")
            @RequestParam(required = false) UUID posteSanteId
    ) {
        UUID sageFemmeId = SecurityUtils.requireCurrentUserId();
        var data = generateUseCase.generate(new GenerateRapportMensuelUseCase.Command(mois, annee, posteSanteId, sageFemmeId));
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ApiPaths.API_V1 + "/rapports/{id}")
                .buildAndExpand(data.rapport().getId()).toUri();
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(RapportMensuelWebMapper.toResponse(data));
    }

    @Operation(summary = "Recupere un rapport par son id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Rapport introuvable", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('SAGE_FEMME', 'ADMIN_POSTE', 'SUPER_ADMIN')")
    @GetMapping("/{id}")
    public RapportMensuelResponse byId(@PathVariable UUID id) {
        return RapportMensuelWebMapper.toResponse(getUseCase.byId(id));
    }

    @Operation(
            summary = "Recherche le rapport d'une periode pour la sage-femme connectee",
            description = "Renvoie 404 si aucun rapport n'a encore ete genere pour cette periode."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Aucun rapport pour cette periode", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('SAGE_FEMME')")
    @GetMapping
    public ResponseEntity<RapportMensuelResponse> byPeriode(
            @RequestParam @Min(1) @Max(12) int mois,
            @RequestParam @Min(2000) @Max(2100) int annee
    ) {
        UUID sageFemmeId = SecurityUtils.requireCurrentUserId();
        return getUseCase.byPeriode(mois, annee, sageFemmeId)
                .map(RapportMensuelWebMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    @Operation(summary = "Valide un rapport (BROUILLON -> VALIDE)")
    @PreAuthorize("hasRole('SAGE_FEMME')")
    @PostMapping("/{id}/validate")
    public RapportMensuelResponse validate(@PathVariable UUID id) {
        RapportMensuel updated = submitUseCase.valider(id);
        return RapportMensuelWebMapper.toResponse(getUseCase.byId(updated.getId()));
    }

    @Transactional
    @Operation(
            summary = "Soumet le rapport au District (BROUILLON/VALIDE -> SOUMIS)",
            description = "Bouton 'Soumettre au District' du frontend. Apres soumission, le rapport est gele."
    )
    @PreAuthorize("hasRole('SAGE_FEMME')")
    @PostMapping("/{id}/submit")
    public RapportMensuelResponse submit(@PathVariable UUID id) {
        RapportMensuel updated = submitUseCase.soumettre(id);
        return RapportMensuelWebMapper.toResponse(getUseCase.byId(updated.getId()));
    }
}
