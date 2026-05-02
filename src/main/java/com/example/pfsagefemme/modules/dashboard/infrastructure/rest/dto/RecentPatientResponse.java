package com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Ligne du tableau 'Dernieres patientes enregistrees (toutes regions)'")
public record RecentPatientResponse(
        UUID patienteId,
        String numeroDossier,
        String region,
        String posteNom,
        LocalDate dateInscription
) {}
