package com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Informations du poste de sante - ecran Mon poste de sante")
public record MonPosteResponse(
        UUID id,
        String identifiantExterne,
        String nom,
        String district,
        String region,
        String adresse,
        String telephone,
        String responsableNom,
        String responsableTitre,
        String responsableEmail,
        String responsableTelephone,
        boolean actif
) {}
