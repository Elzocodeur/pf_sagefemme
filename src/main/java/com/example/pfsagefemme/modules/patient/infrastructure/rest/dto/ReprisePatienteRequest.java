package com.example.pfsagefemme.modules.patient.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Donnees de reprise d'une patiente arretee/abandonnee")
public record ReprisePatienteRequest(
        @Schema(description = "Observations libres a propos de la reprise")
        @Size(max = 500) String observations
) {}
