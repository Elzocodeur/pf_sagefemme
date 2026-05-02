package com.example.pfsagefemme.modules.user.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Compteurs des 4 cards de l'ecran 'Utilisateurs'")
public record UserStatsResponse(long total, long superAdmins, long chefsDePoste, long sagesFemmes) {}
