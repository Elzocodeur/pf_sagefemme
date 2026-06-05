package com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "KPI du rapport mensuel du poste")
public record KpiRapportResponse(
        long value,
        String label,
        @Schema(description = "Evolution vs mois precedent en %, null si non calculable") Double evolutionPercent
) {}
