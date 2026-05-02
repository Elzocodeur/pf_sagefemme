package com.example.pfsagefemme.modules.postesante.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Poste de sante - vue detaillee")
public record PosteSanteResponse(
        UUID id,
        @Schema(description = "Identifiant externe humain (ex: PS-SEN-2024-082)") String identifiantExterne,
        String nom,
        String adresse,
        String telephone,
        String district,
        String region,
        Boolean actif,
        String responsableNom,
        String responsableTitre,
        String responsableEmail,
        String responsableTelephone
) {}
