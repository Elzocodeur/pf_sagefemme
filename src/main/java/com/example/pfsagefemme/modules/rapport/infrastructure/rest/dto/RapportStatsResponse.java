package com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Compteurs des 5 cards en haut du rapport mensuel")
public record RapportStatsResponse(
        long nouvellesPv,
        long anciennesPv,
        long actives,
        long inactives,
        long pvvihActives
) {}
