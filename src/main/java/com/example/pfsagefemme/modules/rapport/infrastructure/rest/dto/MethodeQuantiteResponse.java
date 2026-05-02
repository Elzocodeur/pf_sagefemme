package com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto;

import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Methode + quantite (utilise pour LARC et Consommation Hormonaux)")
public record MethodeQuantiteResponse(
        MethodeContraceptiveEnum methode,
        String label,
        long quantite
) {}
