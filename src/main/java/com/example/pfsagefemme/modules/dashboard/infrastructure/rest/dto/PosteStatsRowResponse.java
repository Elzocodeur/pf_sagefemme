package com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Ligne du tableau 'Vue par poste de sante'")
public record PosteStatsRowResponse(
        UUID posteSanteId,
        String posteNom,
        String district,
        String region,
        long total,
        long actives,
        long abandons,
        long pvvih,
        long sagesFemmes,
        @Schema(description = "Taux d'activite : actives / total (pourcentage entier)")
        int tauxActivitePercent
) {}
