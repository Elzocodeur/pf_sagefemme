package com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Ligne du tableau 'Section 5.2 : Recapitulatif SNSR par District'")
public record RapportNationalPosteRowResponse(
        UUID posteSanteId,
        String posteNom,
        long nouvellesPv,
        long anciennesPv,
        long actives,
        long inactives,
        long pvvih,
        long total
) {}
