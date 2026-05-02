package com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Rapport National PF complet pour un mois donne")
public record RapportNationalResponse(
        int mois,
        int annee,
        String periodeLabel,
        RapportNationalStatsResponse stats,
        @Schema(description = "Section 5.2 - Recapitulatif SNSR par District / Poste")
        List<RapportNationalPosteRowResponse> postesRows,
        RapportNationalTotalResponse totalNational,
        @Schema(description = "Detail des Methodes de Longue Duree (LARC)")
        List<MethodeQuantiteResponse> larcDetail,
        @Schema(description = "Consommation des Intrants - Hormonaux")
        List<MethodeQuantiteResponse> hormonauxConsommes
) {}
