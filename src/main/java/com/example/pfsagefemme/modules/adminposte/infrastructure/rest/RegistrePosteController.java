package com.example.pfsagefemme.modules.adminposte.infrastructure.rest;

import com.example.pfsagefemme.modules.adminposte.domain.port.in.GetRegistrePosteUseCase;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto.RegistrePosteResponse;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.mapper.ChefPosteWebMapper;
import com.example.pfsagefemme.modules.user.domain.model.entity.AdminPoste;
import com.example.pfsagefemme.modules.user.domain.port.in.GetCurrentUserUseCase;
import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.infrastructure.security.SecurityUtils;
import com.example.pfsagefemme.shared.infrastructure.web.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "8 - Chef de poste : Fiches PF du poste (ADMIN_POSTE)",
        description = "Ecran 'Registre complet' - toutes les patientes du poste avec methode, sage-femme et prochain RDV")
@RestController
@RequestMapping(ApiPaths.API_V1 + "/chef-poste/patientes")
@RequiredArgsConstructor
public class RegistrePosteController {

    private final GetRegistrePosteUseCase useCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;

    @Operation(
            summary = "Registre complet des patientes du poste",
            description = """
                    Retourne toutes les patientes du poste avec :
                      - N° dossier, nom/prenom, age, sage-femme, methode, statut, PVVIH, prochain RDV
                    Filtres optionnels : methode contraceptive (onglets Pilule, DIU, Injectables, Implant, PVVIH).
                    """
    )
    @ApiResponse(responseCode = "200", description = "OK")
    @PreAuthorize("hasRole('ADMIN_POSTE')")
    @GetMapping
    public RegistrePosteResponse load(
            @Parameter(description = "Filtre par methode (null = toutes)")
            @RequestParam(required = false) MethodeContraceptiveEnum methode
    ) {
        return ChefPosteWebMapper.toResponse(useCase.load(requirePosteId(), methode));
    }

    private UUID requirePosteId() {
        var u = getCurrentUserUseCase.byId(SecurityUtils.requireCurrentUserId());
        if (u instanceof AdminPoste ap && ap.getPosteSante() != null) {
            return ap.getPosteSante().getId();
        }
        throw new IllegalStateException("Admin poste sans poste rattaché");
    }
}
