package com.example.pfsagefemme.modules.user.infrastructure.rest;

import com.example.pfsagefemme.modules.user.domain.port.in.DeactivateUserUseCase;
import com.example.pfsagefemme.modules.user.domain.port.in.GetUserStatsUseCase;
import com.example.pfsagefemme.modules.user.domain.port.in.ListUsersUseCase;
import com.example.pfsagefemme.modules.user.infrastructure.rest.dto.UserResponse;
import com.example.pfsagefemme.modules.user.infrastructure.rest.dto.UserStatsResponse;
import com.example.pfsagefemme.modules.user.infrastructure.rest.mapper.UserWebMapper;
import com.example.pfsagefemme.shared.domain.enums.RoleEnum;
import com.example.pfsagefemme.shared.infrastructure.web.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "5 - Utilisateurs (administration)", description = "Listing, stats, activation et desactivation des utilisateurs")
@RestController
@RequestMapping(ApiPaths.API_V1 + "/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final ListUsersUseCase listUsersUseCase;
    private final DeactivateUserUseCase deactivateUserUseCase;
    private final GetUserStatsUseCase getUserStatsUseCase;

    @Operation(summary = "Compteurs des 4 cards de l'ecran 'Utilisateurs'")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_POSTE')")
    @GetMapping("/stats")
    public UserStatsResponse stats() {
        return UserWebMapper.toResponse(getUserStatsUseCase.stats());
    }

    @Operation(summary = "Liste les utilisateurs (filtre optionnel par role)")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_POSTE')")
    @GetMapping
    public List<UserResponse> list(
            @Parameter(description = "Filtre par role : SUPER_ADMIN | ADMIN_POSTE | SAGE_FEMME")
            @RequestParam(required = false) RoleEnum role
    ) {
        return listUsersUseCase.list(role).stream().map(UserWebMapper::toResponse).toList();
    }

    @Operation(summary = "Desactive un utilisateur (soft delete)")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_POSTE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        deactivateUserUseCase.deactivate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Reactive un utilisateur")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_POSTE')")
    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        deactivateUserUseCase.activate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
