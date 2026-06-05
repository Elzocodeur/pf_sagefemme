package com.example.pfsagefemme.modules.adminposte.infrastructure.rest;

import com.example.pfsagefemme.modules.adminposte.domain.port.in.GetMonPosteUseCase;
import com.example.pfsagefemme.modules.adminposte.domain.port.in.UpdateMonPosteUseCase;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto.MonPosteResponse;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto.UpdateMonPosteRequest;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.mapper.ChefPosteWebMapper;
import com.example.pfsagefemme.modules.user.domain.model.entity.AdminPoste;
import com.example.pfsagefemme.modules.user.domain.port.in.GetCurrentUserUseCase;
import com.example.pfsagefemme.shared.infrastructure.security.SecurityUtils;
import com.example.pfsagefemme.shared.infrastructure.web.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "10 - Chef de poste : Mon poste de sante (ADMIN_POSTE)",
        description = "Ecran 'Configuration de l'unite' - lecture et modification des informations du poste")
@RestController
@RequestMapping(ApiPaths.API_V1 + "/chef-poste/mon-poste")
@RequiredArgsConstructor
public class MonPosteController {

    private final GetMonPosteUseCase getMonPosteUseCase;
    private final UpdateMonPosteUseCase updateMonPosteUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;

    @Operation(
            summary = "Informations du poste de sante",
            description = "Retourne les donnees institutionnelles du poste : nom, district, region, responsable, contacts."
    )
    @ApiResponse(responseCode = "200", description = "OK")
    @PreAuthorize("hasRole('ADMIN_POSTE')")
    @GetMapping
    public MonPosteResponse get() {
        return ChefPosteWebMapper.toResponse(getMonPosteUseCase.load(requirePosteId()));
    }

    @Operation(
            summary = "Modifier les informations du poste",
            description = "Met a jour les champs institutionnels : nom, district, region, responsable, telephone, email. Seuls les champs fournis sont modifies (PATCH semantics)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Modifications enregistrees"),
            @ApiResponse(responseCode = "404", description = "Poste introuvable", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PreAuthorize("hasRole('ADMIN_POSTE')")
    @PatchMapping
    public MonPosteResponse update(@RequestBody UpdateMonPosteRequest request) {
        var command = new UpdateMonPosteUseCase.Command(
                request.nom(),
                request.district(),
                request.region(),
                request.responsableNom(),
                request.telephone(),
                request.responsableEmail(),
                request.responsableTelephone(),
                request.responsableTitre()
        );
        return ChefPosteWebMapper.toResponse(updateMonPosteUseCase.update(requirePosteId(), command));
    }

    private UUID requirePosteId() {
        var u = getCurrentUserUseCase.byId(SecurityUtils.requireCurrentUserId());
        if (u instanceof AdminPoste ap && ap.getPosteSante() != null) {
            return ap.getPosteSante().getId();
        }
        throw new IllegalStateException("Admin poste sans poste rattaché");
    }
}
