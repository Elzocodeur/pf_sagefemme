package com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Carte KPI")
public record KpiCardResponse(long value, String label, String description) {}
