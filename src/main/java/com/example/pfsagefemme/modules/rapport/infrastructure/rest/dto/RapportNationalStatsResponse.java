package com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Compteurs des 5 cards en haut du Rapport National PF")
public record RapportNationalStatsResponse(
        long nouvellesPv,
        @Schema(description = "Variation % vs mois precedent (peut etre null)") Double deltaNouvellesPercent,
        long anciennesPv,
        long actives,
        long inactives,
        long pvvihActives
) {}
