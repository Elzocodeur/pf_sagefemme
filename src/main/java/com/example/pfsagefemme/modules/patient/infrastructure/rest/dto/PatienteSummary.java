package com.example.pfsagefemme.modules.patient.infrastructure.rest.dto;

import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Patiente - vue resumee pour les listings")
public record PatienteSummary(
        UUID id,
        String numeroDossier,
        String prenom,
        String nom,
        Integer age,
        StatutPatienteEnum statutPatiente,
        Boolean pvvih,
        LocalDate dateInscription
) {}
