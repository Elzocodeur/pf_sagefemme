package com.example.pfsagefemme.modules.user.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Identifiants de connexion")
public record LoginRequest(
        @Schema(example = "superadmin@planifem.local", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Email String email,

        @Schema(example = "ChangeMe!2026", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String motDePasse
) {}
