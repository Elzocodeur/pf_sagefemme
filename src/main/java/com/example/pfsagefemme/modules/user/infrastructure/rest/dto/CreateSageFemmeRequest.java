package com.example.pfsagefemme.modules.user.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Donnees de creation d'une sage-femme")
public record CreateSageFemmeRequest(
        @NotBlank @Size(max = 100) String nom,
        @NotBlank @Size(max = 100) String prenom,
        @NotBlank @Email @Size(max = 150) String email,
        @Schema(description = "Identifiant court de connexion (ex: sf1)")
        @Size(max = 50) String username,
        @NotBlank @Size(min = 8, max = 100) String motDePasse,
        @Size(max = 30) String telephone,
        @Size(max = 50) String matricule,
        @Size(max = 100) String specialite,
        @Schema(description = "Poste d'affectation - obligatoire pour SUPER_ADMIN, ignore pour ADMIN_POSTE (utilise son propre poste)")
        java.util.UUID posteSanteId
) {}
