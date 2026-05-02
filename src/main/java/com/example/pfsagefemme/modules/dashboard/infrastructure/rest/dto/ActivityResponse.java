package com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Activite des 7 derniers jours (lundi -> dimanche)")
public record ActivityResponse(long total, List<ActivityDayResponse> days) {}
