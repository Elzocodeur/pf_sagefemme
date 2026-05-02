package com.example.pfsagefemme.modules.user.infrastructure.rest;

import com.example.pfsagefemme.modules.user.domain.model.entity.SuperAdmin;
import com.example.pfsagefemme.modules.user.domain.port.in.CreateSuperAdminUseCase;
import com.example.pfsagefemme.modules.user.infrastructure.rest.dto.CreateSuperAdminRequest;
import com.example.pfsagefemme.modules.user.infrastructure.rest.dto.UserResponse;
import com.example.pfsagefemme.modules.user.infrastructure.rest.mapper.UserWebMapper;
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

@Tag(name = "2 - Super admins (SUPER_ADMIN)", description = "Creation des super-administrateurs nationaux")
@RestController
@RequestMapping(ApiPaths.API_V1 + "/admin/super-admins")
@RequiredArgsConstructor
public class SuperAdminController {

    private final CreateSuperAdminUseCase createSuperAdminUseCase;

    @Operation(
            summary = "Cree un SuperAdmin",
            description = "Reserve aux SUPER_ADMIN existants. Un ADMIN_POSTE n'a PAS le droit de creer un SuperAdmin."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "SuperAdmin cree"),
            @ApiResponse(responseCode = "403", description = "Reserve aux SUPER_ADMIN", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "422", description = "Email ou identifiant deja utilise", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateSuperAdminRequest request) {
        SuperAdmin created = createSuperAdminUseCase.create(UserWebMapper.toCommand(request));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(UserWebMapper.toResponse(created));
    }
}
