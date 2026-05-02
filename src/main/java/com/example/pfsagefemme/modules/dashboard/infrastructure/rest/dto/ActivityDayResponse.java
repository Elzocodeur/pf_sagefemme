package com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Une barre de l'histogramme 'Activite des 7 derniers jours'")
public record ActivityDayResponse(
        LocalDate date,
        @Schema(example = "Lun") String label,
        long count
) {}
