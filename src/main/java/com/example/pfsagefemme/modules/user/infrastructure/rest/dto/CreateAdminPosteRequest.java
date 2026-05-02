package com.example.pfsagefemme.modules.user.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(description = "Donnees de creation d'un administrateur de poste de sante")
public record CreateAdminPosteRequest(
        @NotBlank @Size(max = 100) String nom,
        @NotBlank @Size(max = 100) String prenom,
        @NotBlank @Email @Size(max = 150) String email,
        @Schema(description = "Identifiant court de connexion (ex: chef_cs1)")
        @Size(max = 50) String username,
        @NotBlank @Size(min = 8, max = 100) String motDePasse,
        @Size(max = 30) String telephone,
        @NotNull @Schema(description = "Id du poste de sante a administrer") UUID posteSanteId
) {}
