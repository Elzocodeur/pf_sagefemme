package com.example.pfsagefemme.modules.patient.infrastructure.rest;

import com.example.pfsagefemme.modules.patient.domain.model.PatientFolder;
import com.example.pfsagefemme.modules.patient.domain.port.in.CreatePatientFolderUseCase;
import com.example.pfsagefemme.modules.patient.domain.port.in.GetPatientFolderUseCase;
import com.example.pfsagefemme.modules.patient.domain.port.in.SearchPatientsUseCase;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.CreatePatientFolderRequest;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.PatientFolderResponse;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.PatienteSummary;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.mapper.PatienteWebMapper;
import com.example.pfsagefemme.shared.infrastructure.security.SecurityUtils;
import com.example.pfsagefemme.shared.infrastructure.web.ApiPaths;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "6 - Patientes (SAGE_FEMME)", description = "Creation et consultation des dossiers patientes")
@RestController
@RequestMapping(ApiPaths.API_V1 + "/patientes")
@RequiredArgsConstructor
public class PatienteController {

    private final CreatePatientFolderUseCase createPatientFolderUseCase;
    private final GetPatientFolderUseCase getPatientFolderUseCase;
    private final SearchPatientsUseCase searchPatientsUseCase;

    @Operation(
            summary = "Cree un dossier patient complet",
            description = """
                    Cree atomiquement la patiente, sa fiche de consultation (antecedents + methode choisie)
                    et la 1ere visite. La patiente est automatiquement rattachee au poste de sante de la
                    sage-femme appelante.

                    Reserve aux SAGE_FEMME (la sage-femme doit etre rattachee a un poste).
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Dossier cree"),
            @ApiResponse(responseCode = "400", description = "Validation echouee", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "403", description = "Reserve aux SAGE_FEMME", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "422", description = "Sage-femme non rattachee a un poste", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasRole('SAGE_FEMME')")
    @PostMapping
    public ResponseEntity<PatientFolderResponse> create(@Valid @RequestBody CreatePatientFolderRequest request) {
        PatientFolder folder = createPatientFolderUseCase.create(
                SecurityUtils.requireCurrentUserId(),
                PatienteWebMapper.toCommand(request)
        );
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(folder.patiente().getId()).toUri();
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(PatienteWebMapper.toResponse(folder));
    }

    @Operation(summary = "Recupere le dossier complet d'une patiente (patiente + fiche + visites)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Patiente introuvable", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasAnyRole('SAGE_FEMME', 'ADMIN_POSTE', 'SUPER_ADMIN')")
    @GetMapping("/{id}")
    public PatientFolderResponse getFolder(@PathVariable UUID id) {
        return PatienteWebMapper.toResponse(getPatientFolderUseCase.byPatienteId(id));
    }

    @Operation(
            summary = "Recherche de patientes (par nom ou prenom)",
            description = "Renvoie une liste resumee. Le filtre par poste est optionnel - si null, recherche globale."
    )
    @PreAuthorize("hasAnyRole('SAGE_FEMME', 'ADMIN_POSTE', 'SUPER_ADMIN')")
    @GetMapping
    public List<PatienteSummary> search(
            @Parameter(description = "Terme de recherche (partiel, insensible a la casse)")
            @RequestParam(required = false) String q,
            @Parameter(description = "Filtre par poste de sante")
            @RequestParam(required = false) UUID posteSanteId
    ) {
        return searchPatientsUseCase.search(q, posteSanteId).stream()
                .map(PatienteWebMapper::toSummary)
                .toList();
    }
}
