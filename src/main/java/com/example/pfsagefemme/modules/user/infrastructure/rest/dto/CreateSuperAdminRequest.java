package com.example.pfsagefemme.modules.user.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Donnees de creation d'un super administrateur (national)")
public record CreateSuperAdminRequest(
        @NotBlank @Size(max = 100) String nom,
        @NotBlank @Size(max = 100) String prenom,
        @NotBlank @Email @Size(max = 150) String email,
        @Schema(description = "Identifiant court (ex: admin)")
        @Size(max = 50) String username,
        @NotBlank @Size(min = 8, max = 100) String motDePasse,
        @Size(max = 30) String telephone,
        @Size(max = 50) String niveauAcces
) {}
