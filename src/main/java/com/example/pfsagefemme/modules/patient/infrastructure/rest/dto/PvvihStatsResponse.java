package com.example.pfsagefemme.modules.patient.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Compteurs PVVIH affiches sur le dashboard 5.2.3")
public record PvvihStatsResponse(
        @Schema(description = "Total cumule des femmes PVVIH (cumul annuel)") long totalAnnuel,
        @Schema(description = "PVVIH actuellement sous traitement & suivi") long actives,
        @Schema(description = "Nouvelles PVVIH enregistrees ce mois-ci") long nouvellesCeMois
) {}
