package com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Reponse complete du Dashboard sage-femme : 5 cards + activite + abandons + actives recentes")
public record SageFemmeDashboardResponse(
        StatCardResponse total,
        StatCardResponse actives,
        StatCardResponse arretees,
        StatCardResponse abandons,
        StatCardResponse pvvih,
        ActivityResponse activity,
        AbandonsRelanceResponse abandonsRelance,
        List<ActiveRecenteResponse> activesRecentes
) {}
