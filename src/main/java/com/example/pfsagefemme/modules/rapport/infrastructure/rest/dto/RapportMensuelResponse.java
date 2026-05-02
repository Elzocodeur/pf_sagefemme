package com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto;

import com.example.pfsagefemme.shared.domain.enums.StatutRapportEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Rapport mensuel PF complet (toutes les sections + matrice + cycle de vie)")
public record RapportMensuelResponse(
        UUID id,
        int mois,
        int annee,
        String periodeLabel,
        UUID posteSanteId,
        UUID sageFemmeId,
        String signedBy,
        StatutRapportEnum statut,
        LocalDateTime dateGeneration,

        RapportStatsResponse stats,

        @Schema(description = "5.2.1 Nouvelles dans le programme") List<MethodeLineResponse> section521,
        @Schema(description = "5.2.1bis Anciennes (retours) - vide tant que l'audit log n'est pas implemente")
        List<MethodeLineResponse> section521bis,
        @Schema(description = "5.2.2 Actives selon la methode") List<MethodeLineResponse> section522,
        @Schema(description = "5.2.3 PVVIH actives PTME") PvvihSectionResponse section523,
        @Schema(description = "5.2.4 Inactives selon la methode") List<MethodeLineResponse> section524,
        @Schema(description = "Matrice 5.2.1 - changements de methode (DE -> VERS), cellules non nulles uniquement")
        List<MatriceCellResponse> matriceChangements
) {}
