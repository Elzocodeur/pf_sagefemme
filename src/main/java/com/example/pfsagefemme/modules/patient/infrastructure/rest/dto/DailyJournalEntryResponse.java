package com.example.pfsagefemme.modules.patient.infrastructure.rest.dto;

import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import com.example.pfsagefemme.shared.domain.enums.TypeVisiteEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Ligne du tableau 'Liste des patientes consultees'")
public record DailyJournalEntryResponse(
        UUID visiteId,
        UUID patienteId,
        String numeroDossier,
        String prenom,
        String nom,
        Integer age,
        Boolean pvvih,
        StatutPatienteEnum statutPatiente,
        TypeVisiteEnum typeVisite,
        MethodeContraceptiveEnum methodePf,
        @Schema(description = "Tension arterielle", example = "120/80") String tensionArterielle,
        @Schema(description = "Poids en kg", example = "64") Double poids,
        LocalDate prochainRdv
) {}
