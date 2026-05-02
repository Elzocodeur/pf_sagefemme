package com.example.pfsagefemme.modules.patient.infrastructure.rest.dto;

import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Ligne de tableau des arretees / abandons")
public record PatientStatusRowResponse(
        UUID id,
        String numeroDossier,
        String prenom,
        String nom,
        String adresse,
        StatutPatienteEnum statutPatiente,
        MethodeContraceptiveEnum methodePf,
        @Schema(description = "Date d'arret pour les ARRETEES") LocalDate dateArret,
        String motifArret,
        @Schema(description = "Date du dernier RDV planifie (ou null)") LocalDate dernierRdv
) {}
