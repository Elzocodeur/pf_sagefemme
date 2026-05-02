package com.example.pfsagefemme.modules.patient.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Compteurs de l'ecran 'Statuts patientes'")
public record PatientStatusStatsResponse(
        @Schema(description = "Total patientes arretees (toutes periodes)") long arretees,
        @Schema(description = "Patientes arretees ce mois") long arreteesCeMois,
        @Schema(description = "Patientes ACTIVE sans visite depuis le seuil (defaut 90 jours)") long abandons,
        @Schema(description = "Anciennes patientes reprises (placeholder, 0 pour l'instant)") long anciennesReprises
) {}
