package com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto;

import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Patiente active recente avec methode - section 'Actives recentes'")
public record ActiveRecentePosteResponse(
        UUID id,
        String numeroDossier,
        String prenom,
        String nom,
        MethodeContraceptiveEnum methode,
        @Schema(description = "Libelle lisible de la methode") String methodeLabel,
        StatutPatienteEnum statut
) {}
