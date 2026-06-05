package com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Vue generale - Statistiques consolidees du poste (ecran Statistiques poste)")
public record StatsPosteResponse(
        KpiCardResponse total,
        KpiCardResponse actives,
        KpiCardResponse arretees,
        KpiCardResponse abandons,
        KpiCardResponse pvvih,
        @Schema(description = "Top actives recentes avec methode") List<ActiveRecentePosteResponse> activesRecentes,
        @Schema(description = "Patientes a surveiller (abandons + arretees)") List<ASurveillerResponse> aSurveiller,
        @Schema(description = "Repartition par categorie de methode") List<MethodeCountResponse> repartitionParMethode,
        @Schema(description = "Taux de retention en % (actives / total)") long tauxRetentionPercent,
        @Schema(description = "Categorie de methode la plus sollicitee") String methodePlusSollicitee,
        @Schema(description = "Evolution mensuelle des nouvelles PV en %") double evolutionMensuellePercent
) {}
