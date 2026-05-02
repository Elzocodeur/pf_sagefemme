package com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto;

import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Cellule de la matrice 5.2.1 (DE -> VERS)")
public record MatriceCellResponse(
        MethodeContraceptiveEnum source,
        MethodeContraceptiveEnum cible,
        long nombre
) {}
