package com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Section 5.2.3 - Femmes seropositives sous contraception moderne (PTME)")
public record PvvihSectionResponse(
        long tranche10_14,
        long tranche15_19,
        long tranche20_24,
        long tranche25_49,
        long tranche50Plus,
        long total
) {}
