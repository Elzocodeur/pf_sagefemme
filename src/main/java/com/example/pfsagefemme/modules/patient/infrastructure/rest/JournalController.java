package com.example.pfsagefemme.modules.patient.infrastructure.rest;

import com.example.pfsagefemme.modules.patient.domain.port.in.GetDailyJournalUseCase;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.DailyJournalResponse;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.mapper.DailyJournalWebMapper;
import com.example.pfsagefemme.shared.infrastructure.web.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.PastOrPresent;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@Tag(name = "10 - Journal du jour", description = "Vue journaliere : compteurs + liste filtrable des patientes consultees")
@RestController
@RequestMapping(ApiPaths.API_V1 + "/journal")
@RequiredArgsConstructor
public class JournalController {

    private final GetDailyJournalUseCase getDailyJournalUseCase;

    @Operation(
            summary = "Journal du jour",
            description = """
                    Retourne en une seule reponse :
                      - les 6 compteurs (cards) du jour : total, nouvelles, anciennes, actives, arretees, abandons
                      - la liste filtree des visites du jour

                    Le filtre `filter` correspond aux pills sous le calendrier (Toutes / Nouvelle PV / Ancienne PV / Active / Arretee / Abandon / PVVIH).
                    """
    )
    @PreAuthorize("hasAnyRole('SAGE_FEMME', 'ADMIN_POSTE', 'SUPER_ADMIN')")
    @GetMapping
    public DailyJournalResponse get(
            @Parameter(description = "Date du journal (defaut : aujourd'hui)")
            @RequestParam(required = false) @PastOrPresent
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,

            @Parameter(description = "Filtre par poste (null = global)")
            @RequestParam(required = false) UUID posteSanteId,

            @Parameter(description = "Filtre des pills sous le calendrier")
            @RequestParam(defaultValue = "ALL") GetDailyJournalUseCase.Filter filter
    ) {
        LocalDate target = date == null ? LocalDate.now() : date;
        return DailyJournalWebMapper.toResponse(
                getDailyJournalUseCase.forDate(target, posteSanteId, filter)
        );
    }
}
