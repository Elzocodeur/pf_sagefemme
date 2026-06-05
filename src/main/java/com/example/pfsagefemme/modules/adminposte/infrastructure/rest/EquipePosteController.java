package com.example.pfsagefemme.modules.adminposte.infrastructure.rest;

import com.example.pfsagefemme.modules.adminposte.domain.port.in.GetEquipePosteUseCase;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto.EquipePosteResponse;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.mapper.ChefPosteWebMapper;
import com.example.pfsagefemme.modules.user.domain.model.entity.AdminPoste;
import com.example.pfsagefemme.modules.user.domain.port.in.GetCurrentUserUseCase;
import com.example.pfsagefemme.shared.infrastructure.security.SecurityUtils;
import com.example.pfsagefemme.shared.infrastructure.web.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "6 - Chef de poste : Sages-femmes (ADMIN_POSTE)",
        description = "Ecran 'Effectif' - liste des sages-femmes du poste avec stats patientes et activite")
@RestController
@RequestMapping(ApiPaths.API_V1 + "/chef-poste/sages-femmes")
@RequiredArgsConstructor
public class EquipePosteController {

    private final GetEquipePosteUseCase useCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;

    @Operation(
            summary = "Effectif du poste - liste des sages-femmes avec statistiques",
            description = """
                    Retourne pour chaque sage-femme du poste :
                      - matricule (N°), nom complet, identifiant, nb patientes, dernière activité, statut
                    Plus les indicateurs de bas de page : activité SF, dernier rapport, conformité.
                    """
    )
    @ApiResponse(responseCode = "200", description = "OK")
    @PreAuthorize("hasRole('ADMIN_POSTE')")
    @GetMapping
    public EquipePosteResponse load() {
        return ChefPosteWebMapper.toResponse(useCase.load(requirePosteId()));
    }

    private UUID requirePosteId() {
        var u = getCurrentUserUseCase.byId(SecurityUtils.requireCurrentUserId());
        if (u instanceof AdminPoste ap && ap.getPosteSante() != null) {
            return ap.getPosteSante().getId();
        }
        throw new IllegalStateException("Admin poste sans poste rattaché");
    }
}
