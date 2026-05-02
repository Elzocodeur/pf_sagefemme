package com.example.pfsagefemme.modules.consultation.infrastructure.rest.dto;

import com.example.pfsagefemme.shared.domain.enums.StatutVisiteEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Visite de suivi - vue detaillee")
public record VisiteDetailResponse(
        UUID id,
        UUID patienteId,
        UUID ficheConsultationId,
        UUID sageFemmeId,
        LocalDate date,
        LocalDate dateDernieresRegles,
        Double poids,
        String tensionArterielle,
        String observations,
        String unitesFournies,
        LocalDate dateProchainRdv,
        StatutVisiteEnum statut
) {}
