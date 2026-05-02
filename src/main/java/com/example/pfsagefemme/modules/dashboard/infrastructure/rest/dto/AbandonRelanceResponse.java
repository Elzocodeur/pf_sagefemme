package com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Une carte 'Abandon a relancer' (priorite RdV manques > 15j)")
public record AbandonRelanceResponse(
        UUID patienteId,
        String numeroDossier,
        String prenom,
        String nom,
        @Schema(description = "Numero de telephone pour le bouton 'Appeler'") String telephone,
        @Schema(description = "Date du dernier contact (visite ou inscription)") LocalDate dernierContact,
        long joursSansContact
) {}
