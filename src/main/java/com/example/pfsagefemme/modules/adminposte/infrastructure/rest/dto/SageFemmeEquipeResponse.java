package com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Sage-femme avec statistiques - ligne de l'écran Effectif")
public record SageFemmeEquipeResponse(
        UUID id,
        @Schema(description = "Numero de matricule ex: SF-001") String matricule,
        String prenom,
        String nom,
        @Schema(description = "Identifiant de connexion (login)") String username,
        boolean actif,
        @Schema(description = "Nombre total de patientes suivies") int nbPatientes,
        @Schema(description = "Date de la dernière visite enregistrée") LocalDate derniereActivite
) {}
