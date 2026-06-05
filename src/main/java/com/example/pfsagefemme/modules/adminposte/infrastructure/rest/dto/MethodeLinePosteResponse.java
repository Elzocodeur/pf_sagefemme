package com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto;

import com.example.pfsagefemme.shared.domain.enums.AgeBracketEnum;
import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "Ligne tableau rapport (methode x tranches d'age)")
public record MethodeLinePosteResponse(
        MethodeContraceptiveEnum methode,
        String methodeLabel,
        Map<AgeBracketEnum, Long> tranches,
        long total
) {}
