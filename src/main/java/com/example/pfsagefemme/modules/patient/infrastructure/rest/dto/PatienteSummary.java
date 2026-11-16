package com.example.pfsagefemme.modules.patient.infrastructure.rest.dto;

import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
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
        LocalDate dateInscription,
        @Schema(description = "Methode en cours (fiche de consultation)") MethodeContraceptiveEnum methode,
        String methodeLabel,
        String posteSanteNom,
        @Schema(description = "Nombre de changements de methode enregistres") int nombreChangements,
        @Schema(description = "Methode avant le dernier changement (null si jamais changee)") MethodeContraceptiveEnum ancienneMethode,
        String ancienneMethodeLabel,
        @Schema(description = "Date du dernier changement de methode") LocalDate dateDernierChangement,
        @Schema(description = "Motif du dernier changement") String motifDernierChangement
) {}
