package com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Compteurs globaux (5 cards) de l'ecran 'Statistiques nationales'")
public record NationalGlobalStatsResponse(
        long totalPatientes,
        long actives,
        long abandons,
        long pvvih,
        long sagesFemmes
) {}
