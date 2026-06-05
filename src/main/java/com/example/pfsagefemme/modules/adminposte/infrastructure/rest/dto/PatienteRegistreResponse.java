package com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto;

import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Ligne du registre complet - ecran Fiches PF du poste")
public record PatienteRegistreResponse(
        UUID id,
        @Schema(description = "ex: PF-042") String numeroDossier,
        String prenom,
        String nom,
        Integer age,
        @Schema(description = "ex: Mme. Sow") String sageFemmeNomComplet,
        MethodeContraceptiveEnum methode,
        @Schema(description = "Libelle court de la methode") String methodeLabel,
        StatutPatienteEnum statut,
        boolean pvvih,
        LocalDate prochainRdv
) {}
