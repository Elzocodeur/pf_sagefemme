package com.example.pfsagefemme.modules.postesante.infrastructure.rest;

import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
import com.example.pfsagefemme.modules.postesante.domain.port.in.CreatePosteSanteUseCase;
import com.example.pfsagefemme.modules.postesante.domain.port.in.GetPosteSanteUseCase;
import com.example.pfsagefemme.modules.postesante.domain.port.in.GetPostesSanteDashboardUseCase;
import com.example.pfsagefemme.modules.postesante.domain.port.in.TogglePosteSanteUseCase;
import com.example.pfsagefemme.modules.postesante.domain.port.in.UpdatePosteSanteUseCase;
import com.example.pfsagefemme.modules.postesante.infrastructure.rest.dto.CreatePosteSanteRequest;
import com.example.pfsagefemme.modules.postesante.infrastructure.rest.dto.PosteSanteResponse;
import com.example.pfsagefemme.modules.postesante.infrastructure.rest.dto.PosteSanteRowResponse;
import com.example.pfsagefemme.modules.postesante.infrastructure.rest.dto.PostesStatsResponse;
import com.example.pfsagefemme.modules.postesante.infrastructure.rest.dto.UpdatePosteSanteRequest;
import com.example.pfsagefemme.modules.postesante.infrastructure.rest.mapper.PosteSanteWebMapper;
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
import org.springframework.web.bind.annotation.PatchMapping;
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

@Tag(name = "5 - Postes de sante",
        description = "CRUD + dashboard des postes de sante (creation/modif/activation par SUPER_ADMIN, consultation pour tous)")
@RestController
@RequestMapping(ApiPaths.API_V1 + "/postes-sante")
@RequiredArgsConstructor
public class PosteSanteController {

    private final CreatePosteSanteUseCase createUseCase;
    private final UpdatePosteSanteUseCase updateUseCase;
    private final TogglePosteSanteUseCase toggleUseCase;
    private final GetPosteSanteUseCase getUseCase;
    private final GetPostesSanteDashboardUseCase dashboardUseCase;

    @Operation(summary = "Cree un poste de sante avec son responsable", description = "Reserve aux SUPER_ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Poste cree"),
            @ApiResponse(responseCode = "403", description = "Reserve aux SUPER_ADMIN", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<PosteSanteResponse> create(@Valid @RequestBody CreatePosteSanteRequest request) {
        PosteSante created = createUseCase.create(PosteSanteWebMapper.toCommand(request));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(PosteSanteWebMapper.toResponse(created));
    }

    @Operation(summary = "Modifie un poste de sante (champs partiels)")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PatchMapping("/{id}")
    public PosteSanteResponse update(@PathVariable UUID id, @Valid @RequestBody UpdatePosteSanteRequest request) {
        return PosteSanteWebMapper.toResponse(updateUseCase.update(PosteSanteWebMapper.toCommand(id, request)));
    }

    @Operation(summary = "Active un poste de sante")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/{id}/activate")
    public PosteSanteResponse activate(@PathVariable UUID id) {
        return PosteSanteWebMapper.toResponse(toggleUseCase.activate(id));
    }

    @Operation(summary = "Desactive un poste de sante")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/{id}/deactivate")
    public PosteSanteResponse deactivate(@PathVariable UUID id) {
        return PosteSanteWebMapper.toResponse(toggleUseCase.deactivate(id));
    }

    @Operation(summary = "Compteurs des 4 cards de l'ecran 'Postes de sante'")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_POSTE')")
    @GetMapping("/stats")
    public PostesStatsResponse stats() {
        return PosteSanteWebMapper.toResponse(dashboardUseCase.stats());
    }

    @Operation(summary = "Liste enrichie pour le tableau (avec compteurs patientes + SF par poste)",
            description = "Filtre actif=true (Actifs), actif=false (Inactifs), absent (Tous).")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<PosteSanteRowResponse> list(
            @Parameter(description = "Filtre Tous (omettre) / Actifs (true) / Inactifs (false)")
            @RequestParam(required = false) Boolean actif
    ) {
        return dashboardUseCase.list(actif).stream()
                .map(PosteSanteWebMapper::toRow)
                .toList();
    }

    @Operation(summary = "Detail d'un poste de sante")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public PosteSanteResponse getById(@PathVariable UUID id) {
        return PosteSanteWebMapper.toResponse(getUseCase.byId(id));
    }
}
