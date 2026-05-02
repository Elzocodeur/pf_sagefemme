package com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Reponse complete de l'ecran 'Statistiques nationales'")
public record NationalStatsResponse(
        NationalGlobalStatsResponse global,
        List<PosteStatsRowResponse> postesRows,
        long totalNational,
        int tauxMoyenPercent,
        List<RecentPatientResponse> recentPatients
) {}
