package com.example.pfsagefemme.modules.patient.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Compteurs des 6 cards du Journal du jour")
public record DailyJournalStatsResponse(
        long total,
        @Schema(description = "Nouvelles PV inscrites ce jour") long nouvellesPv,
        @Schema(description = "Anciennes PV (inscrites avant) consultees ce jour") long anciennesPv,
        @Schema(description = "Patientes consultees ce jour avec statut ACTIVE") long actives,
        @Schema(description = "Patientes arretees ce jour (dateArret = jour)") long arretees,
        @Schema(description = "Patientes en abandon a la date donnee (sans visite > 90j)") long abandons
) {}
