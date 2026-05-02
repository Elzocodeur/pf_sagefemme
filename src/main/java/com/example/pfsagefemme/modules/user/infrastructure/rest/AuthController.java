package com.example.pfsagefemme.modules.user.infrastructure.rest;

import com.example.pfsagefemme.modules.user.domain.port.in.AuthenticateUseCase;
import com.example.pfsagefemme.modules.user.domain.port.in.ChangePasswordUseCase;
import com.example.pfsagefemme.modules.user.domain.port.in.GetCurrentUserUseCase;
import com.example.pfsagefemme.modules.user.infrastructure.rest.dto.ChangePasswordRequest;
import com.example.pfsagefemme.modules.user.infrastructure.rest.dto.LoginRequest;
import com.example.pfsagefemme.modules.user.infrastructure.rest.dto.LoginResponse;
import com.example.pfsagefemme.modules.user.infrastructure.rest.dto.UserResponse;
import com.example.pfsagefemme.modules.user.infrastructure.rest.mapper.UserWebMapper;
import com.example.pfsagefemme.shared.infrastructure.security.SecurityUtils;
import com.example.pfsagefemme.shared.infrastructure.web.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "1 - Authentication", description = "Login, profil courant, changement de mot de passe")
@RestController
@RequestMapping(ApiPaths.API_V1 + "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticateUseCase authenticateUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;

    @Operation(
            summary = "Connexion (login)",
            description = "Authentifie un utilisateur par email + mot de passe et renvoie un JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentification reussie"),
            @ApiResponse(responseCode = "401", description = "Identifiants invalides ou compte desactive", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @SecurityRequirements // endpoint public (pas de bearer requis)
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        var result = authenticateUseCase.authenticate(UserWebMapper.toCommand(request));
        return LoginResponse.of(result.token(), result.expiresInSeconds(), UserWebMapper.toResponse(result.utilisateur()));
    }

    @Operation(summary = "Profil de l'utilisateur courant")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public UserResponse me() {
        return UserWebMapper.toResponse(getCurrentUserUseCase.byId(SecurityUtils.requireCurrentUserId()));
    }

    @Operation(summary = "Change le mot de passe de l'utilisateur courant")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Mot de passe change"),
            @ApiResponse(responseCode = "401", description = "Mot de passe courant incorrect", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        changePasswordUseCase.changePassword(UserWebMapper.toCommand(SecurityUtils.requireCurrentUserId(), request));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
