package com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Donnees de modification du poste - bouton 'Enregistrer les modifications'")
public record UpdateMonPosteRequest(
        @Schema(description = "Nom du poste") String nom,
        @Schema(description = "District sanitaire") String district,
        @Schema(description = "Region") String region,
        @Schema(description = "Nom du responsable (champ Responsable)") String responsableNom,
        @Schema(description = "Telephone du poste") String telephone,
        @Schema(description = "Email de contact du poste") String responsableEmail,
        @Schema(description = "Telephone direct du responsable") String responsableTelephone,
        @Schema(description = "Titre du responsable (ex: Medecin Chef)") String responsableTitre
) {}
