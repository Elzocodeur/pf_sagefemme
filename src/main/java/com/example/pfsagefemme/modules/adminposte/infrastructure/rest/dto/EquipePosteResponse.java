package com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Effectif complet du poste - écran Sages-femmes")
public record EquipePosteResponse(
        String posteNom,
        @Schema(description = "Nombre total de sages-femmes dans le poste") int totalSagesFemmes,
        List<SageFemmeEquipeResponse> sagesFemmes,
        @Schema(description = "Moyenne de patientes par sage-femme ce mois") double moyennePatientesParSf,
        @Schema(description = "Date de génération du dernier rapport") LocalDate dernierRapportDate,
        @Schema(description = "Vrai si toutes les SF ont au moins une patiente en suivi actif") boolean conformite
) {}
