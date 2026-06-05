package com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Rapport mensuel PF agrege pour le poste - ecran Rapport mensuel")
public record RapportPosteResponse(
        int mois,
        int annee,
        String periodeLabel,
        String posteNom,
        String district,
        @Schema(description = "Nouvelles patientes PV ce mois") KpiRapportResponse nouvellesPv,
        @Schema(description = "Anciennes PV (total - nouvelles)") KpiRapportResponse anciennesPv,
        @Schema(description = "Patientes actives snapshot") KpiRapportResponse actives,
        @Schema(description = "Patientes inactives (arretees + abandons)") KpiRapportResponse inactives,
        @Schema(description = "PVVIH actives snapshot") KpiRapportResponse pvvih,
        @Schema(description = "5.2.1 - Nouvelles utilisatrices par methode et tranche d'age")
        List<MethodeLinePosteResponse> section521,
        @Schema(description = "5.2.2 - Actives (renouvellements) par methode et tranche d'age")
        List<MethodeLinePosteResponse> section522,
        @Schema(description = "5.2.4 - Inactives par methode et tranche d'age")
        List<MethodeLinePosteResponse> section524
) {}
