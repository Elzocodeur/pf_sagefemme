package com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto;

import com.example.pfsagefemme.modules.dashboard.domain.port.in.GetSageFemmeDashboardUseCase.Trend;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Une card KPI du dashboard")
public record StatCardResponse(
        long value,
        @Schema(description = "Variation en pourcentage (peut etre null si non calculable)") Double deltaPercent,
        @Schema(description = "Libelle pret a afficher (ex: '+12% ce mois', 'Alerte hausse')") String deltaLabel,
        @Schema(description = "Tendance pour le styling : UP, DOWN, STABLE, ALERT, PRIORITY") Trend trend
) {}
