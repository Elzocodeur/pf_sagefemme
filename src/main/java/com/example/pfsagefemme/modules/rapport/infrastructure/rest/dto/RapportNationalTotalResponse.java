package com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ligne 'TOTAL NATIONAL' du tableau recapitulatif")
public record RapportNationalTotalResponse(
        long nouvellesPv, long anciennesPv, long actives, long inactives, long pvvih, long total
) {}
