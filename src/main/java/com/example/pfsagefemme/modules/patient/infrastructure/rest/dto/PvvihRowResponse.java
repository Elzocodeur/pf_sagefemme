package com.example.pfsagefemme.modules.patient.infrastructure.rest.dto;

import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.StadeVihOmsEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutSuiviPfEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Ligne enrichie du tableau 'Liste des Patientes PVVIH'")
public record PvvihRowResponse(
        UUID id,
        String numeroDossier,
        String prenom,
        String nom,
        Integer age,
        StatutPatienteEnum statutPatiente,
        StadeVihOmsEnum stadeOms,
        String traitementArv,
        MethodeContraceptiveEnum methodePf,
        StatutSuiviPfEnum statutSuiviPf,
        LocalDate prochainRdv
) {}
