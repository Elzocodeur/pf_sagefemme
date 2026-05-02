package com.example.pfsagefemme.modules.postesante.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Ligne enrichie du tableau 'Postes de sante enregistres'")
public record PosteSanteRowResponse(
        UUID id,
        String identifiantExterne,
        String nom,
        String district,
        String region,
        long nombrePatientes,
        long nombreSagesFemmes,
        Boolean actif
) {}
