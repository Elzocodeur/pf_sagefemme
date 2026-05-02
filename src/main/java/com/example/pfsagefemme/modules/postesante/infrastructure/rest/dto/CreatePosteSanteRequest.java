package com.example.pfsagefemme.modules.postesante.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Donnees de creation d'un poste de sante (avec responsable optionnel)")
public record CreatePosteSanteRequest(
        @NotBlank @Size(max = 150) String nom,
        @Size(max = 255) String adresse,
        @Size(max = 30) String telephone,
        @Size(max = 100) String district,
        @Size(max = 100) String region,

        @Size(max = 150) String responsableNom,
        @Size(max = 100) String responsableTitre,
        @Email @Size(max = 150) String responsableEmail,
        @Size(max = 30) String responsableTelephone,

        @Schema(description = "Configuration : Actif ou Inactif (defaut Actif)")
        Boolean actif
) {}
