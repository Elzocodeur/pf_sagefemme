package com.example.pfsagefemme.modules.postesante.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Compteurs des 4 cards de l'ecran 'Postes de sante'")
public record PostesStatsResponse(
        long postesActifs,
        long totalPatientes,
        long totalSagesFemmes,
        long districtsCouverts
) {}
