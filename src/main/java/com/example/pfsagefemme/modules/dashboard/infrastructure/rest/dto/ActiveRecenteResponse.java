package com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto;

import com.example.pfsagefemme.shared.domain.enums.StatutCliniqueEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Ligne du tableau 'Actives recentes'")
public record ActiveRecenteResponse(
        UUID patienteId,
        String numeroDossier,
        String prenom,
        String nom,
        @Schema(description = "Date de la derniere visite (null si aucune)") LocalDate derniereVisite,
        @Schema(description = "Statut clinique calcule pour le badge") StatutCliniqueEnum statutClinique
) {}
