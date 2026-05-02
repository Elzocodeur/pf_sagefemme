package com.example.pfsagefemme.modules.user.infrastructure.rest.dto;

import com.example.pfsagefemme.shared.domain.enums.RoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Informations publiques d'un utilisateur")
public record UserResponse(
        UUID id,
        String nom,
        String prenom,
        String email,
        @Schema(description = "Identifiant court (alternative a l'email pour le login)") String username,
        String telephone,
        RoleEnum role,
        Boolean actif,
        @Schema(description = "Id du poste de sante de rattachement (null pour SUPER_ADMIN)")
        UUID posteSanteId,
        @Schema(description = "Nom du poste pour affichage tableau") String posteSanteNom,
        @Schema(description = "Matricule (uniquement pour SAGE_FEMME)")
        String matricule
) {}
