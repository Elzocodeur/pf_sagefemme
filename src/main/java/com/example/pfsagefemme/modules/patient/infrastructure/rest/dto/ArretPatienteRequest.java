package com.example.pfsagefemme.modules.patient.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Donnees d'arret d'une patiente")
public record ArretPatienteRequest(
        @Schema(description = "Motif de l'arret (effets secondaires, souhait grossesse...)")
        @Size(max = 255) String motif,

        @Schema(description = "Date de l'arret - defaut aujourd'hui si null")
        @PastOrPresent LocalDate dateArret
) {}
