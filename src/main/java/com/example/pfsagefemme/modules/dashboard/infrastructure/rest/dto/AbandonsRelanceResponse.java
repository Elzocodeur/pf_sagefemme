package com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Bloc 'Abandons a relancer' du dashboard")
public record AbandonsRelanceResponse(
        @Schema(description = "Seuil utilise (en jours) pour qualifier 'a relancer'") int thresholdDays,
        @Schema(description = "Cartes affichees (top N par anciennete)") List<AbandonRelanceResponse> items,
        @Schema(description = "Total de patientes en retard - alimente le lien 'Voir les X dossiers en retard'") long totalEnRetard
) {}
