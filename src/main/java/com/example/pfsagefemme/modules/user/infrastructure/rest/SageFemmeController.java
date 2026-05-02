package com.example.pfsagefemme.modules.user.infrastructure.rest;

import com.example.pfsagefemme.modules.user.domain.model.entity.SageFemme;
import com.example.pfsagefemme.modules.user.domain.port.in.CreateSageFemmeUseCase;
import com.example.pfsagefemme.modules.user.infrastructure.rest.dto.CreateSageFemmeRequest;
import com.example.pfsagefemme.modules.user.infrastructure.rest.dto.UserResponse;
import com.example.pfsagefemme.modules.user.infrastructure.rest.mapper.UserWebMapper;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "4 - Sages-femmes (ADMIN_POSTE, SUPER_ADMIN)", description = "Creation des sages-femmes")
@RestController
@RequestMapping(ApiPaths.API_V1 + "/admin/sages-femmes")
@RequiredArgsConstructor
public class SageFemmeController {

    private final CreateSageFemmeUseCase createSageFemmeUseCase;

    @Operation(
            summary = "Cree une sage-femme",
            description = """
                    Accessible aux SUPER_ADMIN et ADMIN_POSTE.
                    - ADMIN_POSTE : la sage-femme herite automatiquement de son poste, le `posteSanteId` du body est ignore.
                    - SUPER_ADMIN : doit fournir le `posteSanteId` cible dans le body.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sage-femme creee"),
            @ApiResponse(responseCode = "403", description = "Reserve aux SUPER_ADMIN/ADMIN_POSTE", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "422", description = "Email/identifiant deja utilise ou admin non rattache a un poste", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_POSTE')")
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateSageFemmeRequest request) {
        SageFemme created = createSageFemmeUseCase.create(SecurityUtils.requireCurrentUserId(), UserWebMapper.toCommand(request));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(UserWebMapper.toResponse(created));
    }
}
