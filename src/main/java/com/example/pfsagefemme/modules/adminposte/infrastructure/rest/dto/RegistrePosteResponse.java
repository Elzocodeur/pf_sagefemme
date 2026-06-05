package com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Registre complet du poste - ecran Fiches PF du poste")
public record RegistrePosteResponse(
        String posteNom,
        long totalPatientesActives,
        List<PatienteRegistreResponse> patientes,
        @Schema(description = "Patientes avec suivi en retard (prochain RDV depasse)") long avecRetardSuivi,
        @Schema(description = "Nombre d'actions requises") long actionRequise
) {}
