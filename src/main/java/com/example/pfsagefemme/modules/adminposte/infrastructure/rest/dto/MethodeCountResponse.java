package com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto;

import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Repartition par methode - section 'Repartition par methode'")
public record MethodeCountResponse(
        MethodeContraceptiveEnum methode,
        String categorie,
        long count
) {}
