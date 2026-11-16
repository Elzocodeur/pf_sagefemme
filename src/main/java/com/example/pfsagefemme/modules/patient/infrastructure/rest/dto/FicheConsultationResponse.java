package com.example.pfsagefemme.modules.patient.infrastructure.rest.dto;

import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Fiche de consultation - vue resumee retournee avec le dossier")
public record FicheConsultationResponse(
        UUID id,
        String numero,
        LocalDate dateConsultation,
        UUID patienteId,
        UUID sageFemmeId,
        UUID posteSanteId,
        Integer menarchesAge,
        Integer dureeRegles,
        Integer cycleMenstruel,
        LocalDate dateDernieresRegles,
        Integer enfantsVivants,
        Integer enfantsDecedes,
        Integer avortements,
        Integer mortsNes,
        String ageDernierEnfant,
        String allaitementExclusif,
        Double taille,
        Double poids,
        Boolean hypertensionArterielle,
        Boolean diabete,
        Boolean drepanocytose,
        Boolean allergieCuivre,
        String plaintesActuelles,
        String examensComplementaires,
        MethodeContraceptiveEnum methodeChoisie
) {}
