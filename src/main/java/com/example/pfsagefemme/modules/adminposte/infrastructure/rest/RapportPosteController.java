package com.example.pfsagefemme.modules.adminposte.infrastructure.rest;

import com.example.pfsagefemme.modules.adminposte.domain.port.in.GetRapportPosteUseCase;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto.RapportPosteResponse;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.mapper.ChefPosteWebMapper;
import com.example.pfsagefemme.modules.user.domain.model.entity.AdminPoste;
import com.example.pfsagefemme.modules.user.domain.port.in.GetCurrentUserUseCase;
import com.example.pfsagefemme.shared.infrastructure.security.SecurityUtils;
import com.example.pfsagefemme.shared.infrastructure.web.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@Tag(name = "9 - Chef de poste : Rapport mensuel PF (ADMIN_POSTE)",
        description = "Ecran 'Rapport mensuel' - rapport agrege du poste : sections 5.2.1 / 5.2.2 / 5.2.4 + KPIs")
@RestController
@RequestMapping(ApiPaths.API_V1 + "/chef-poste/rapport")
@RequiredArgsConstructor
public class RapportPosteController {

    private final GetRapportPosteUseCase useCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;

    @Operation(
            summary = "Rapport mensuel PF agrege pour le poste",
            description = """
                    Agrege les donnees de toutes les sages-femmes du poste pour un mois donne.
                    Retourne :
                      - 5 KPI (Nvelles PV, Anc. PV, Actives, Inactives, PVVIH)
                      - Section 5.2.1 : nouvelles utilisatrices par methode x tranche d'age
                      - Section 5.2.2 : actives (renouvellements) par methode x tranche d'age
                      - Section 5.2.4 : inactives par methode x tranche d'age
                    Par defaut, mois et annee = mois courant.
                    """
    )
    @ApiResponse(responseCode = "200", description = "OK")
    @PreAuthorize("hasRole('ADMIN_POSTE')")
    @GetMapping
    public RapportPosteResponse load(
            @Parameter(description = "Mois (1-12, defaut = mois courant)")
            @RequestParam(required = false) @Min(1) @Max(12) Integer mois,
            @Parameter(description = "Annee (defaut = annee courante)")
            @RequestParam(required = false) @Min(2000) @Max(2100) Integer annee
    ) {
        int m = mois != null ? mois : LocalDate.now().getMonthValue();
        int a = annee != null ? annee : LocalDate.now().getYear();
        return ChefPosteWebMapper.toResponse(useCase.load(m, a, requirePosteId()));
    }

    private UUID requirePosteId() {
        var u = getCurrentUserUseCase.byId(SecurityUtils.requireCurrentUserId());
        if (u instanceof AdminPoste ap && ap.getPosteSante() != null) {
            return ap.getPosteSante().getId();
        }
        throw new IllegalStateException("Admin poste sans poste rattaché");
    }
}
