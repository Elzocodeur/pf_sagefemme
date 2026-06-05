package com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Patiente a surveiller (abandon ou arretee) - section 'A surveiller'")
public record ASurveillerResponse(
        @Schema(description = "ABANDON ou ARRETEE") String type,
        UUID patienteId,
        String prenom,
        String nom,
        @Schema(description = "Message contextuel (dernier rdv, date arret...)") String message
) {}
