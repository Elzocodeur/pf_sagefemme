package com.example.pfsagefemme.modules.user.infrastructure.rest;

import com.example.pfsagefemme.modules.user.domain.model.entity.AdminPoste;
import com.example.pfsagefemme.modules.user.domain.port.in.CreateAdminPosteUseCase;
import com.example.pfsagefemme.modules.user.infrastructure.rest.dto.CreateAdminPosteRequest;
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

@Tag(name = "3 - Chefs de poste (SUPER_ADMIN)", description = "Creation des chefs de poste (= AdminPoste). Reservee strictement au SUPER_ADMIN.")
@RestController
@RequestMapping(ApiPaths.API_V1 + "/admin/admin-postes")
@RequiredArgsConstructor
public class AdminPosteController {

    private final CreateAdminPosteUseCase createAdminPosteUseCase;

    @Operation(
            summary = "Cree un Chef de poste et l'affecte a un poste de sante",
            description = "Reserve au SUPER_ADMIN. Un chef de poste (ADMIN_POSTE) ne peut PAS creer un autre chef de poste."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Chef de poste cree"),
            @ApiResponse(responseCode = "403", description = "Reserve au SUPER_ADMIN", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "404", description = "Poste de sante introuvable", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "422", description = "Email ou identifiant deja utilise", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateAdminPosteRequest request) {
        AdminPoste created = createAdminPosteUseCase.create(UserWebMapper.toCommand(request));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(UserWebMapper.toResponse(created));
    }
}
