package com.example.pfsagefemme.modules.patient.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Reponse complete du Journal du jour : stats + liste des patientes consultees filtree")
public record DailyJournalResponse(
        LocalDate date,
        DailyJournalStatsResponse stats,
        List<DailyJournalEntryResponse> entries
) {}
