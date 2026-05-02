package com.example.pfsagefemme.modules.patient.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Visite de suivi")
public record VisiteResponse(
        UUID id,
        LocalDate date,
        Double poids,
        String tensionArterielle,
        String observations,
        String unitesFournies,
        LocalDate dateProchainRdv,
        UUID sageFemmeId
) {}
