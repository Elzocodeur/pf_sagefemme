package com.example.pfsagefemme.modules.patient.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Dossier patient complet : patiente + fiche de consultation + visites")
public record PatientFolderResponse(
        PatienteResponse patiente,
        FicheConsultationResponse ficheConsultation,
        List<VisiteResponse> visites
) {}
