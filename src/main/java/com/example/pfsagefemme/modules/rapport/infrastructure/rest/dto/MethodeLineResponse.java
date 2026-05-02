package com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto;

import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Une ligne d'un tableau (5.2.1, 5.2.2, 5.2.4) - 1 methode + 5 tranches d'age + total")
public record MethodeLineResponse(
        MethodeContraceptiveEnum methode,
        @Schema(description = "Categorie d'affichage (Pilules, DIU, Implants...)") String categorie,
        @Schema(description = "Sous-categorie (PP, TCu 380A, ...)") String sousCategorie,
        long tranche10_14,
        long tranche15_19,
        long tranche20_24,
        long tranche25_49,
        long tranche50Plus,
        long total
) {}
