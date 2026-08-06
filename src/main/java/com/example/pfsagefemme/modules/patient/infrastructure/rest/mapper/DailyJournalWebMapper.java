package com.example.pfsagefemme.modules.patient.infrastructure.rest.mapper;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.FicheConsultation;
import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.modules.patient.domain.port.in.GetDailyJournalUseCase;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.DailyJournalEntryResponse;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.DailyJournalResponse;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.DailyJournalStatsResponse;

public final class DailyJournalWebMapper {

    private DailyJournalWebMapper() {}

    public static DailyJournalResponse toResponse(GetDailyJournalUseCase.DailyJournal journal) {
        return new DailyJournalResponse(
                journal.date(),
                toResponse(journal.stats()),
                journal.entries().stream().map(DailyJournalWebMapper::toResponse).toList()
        );
    }

    private static DailyJournalStatsResponse toResponse(GetDailyJournalUseCase.DailyStats s) {
        return new DailyJournalStatsResponse(
                s.total(), s.nouvellesPv(), s.anciennesPv(),
                s.actives(), s.arretees(), s.abandons()
        );
    }

    private static DailyJournalEntryResponse toResponse(GetDailyJournalUseCase.JournalEntry e) {
        Patiente p = e.patiente();
        Visite v = e.visite();
        FicheConsultation f = e.fiche();
        return new DailyJournalEntryResponse(
                v.getId(),
                p.getId(),
                p.getNumeroDossier(),
                p.getPrenom(),
                p.getNom(),
                p.getAge(),
                p.getPvvih(),
                p.getStatutPatiente(),
                e.typeVisite(),
                f == null ? null : f.getMethodeChoisie(),
                e.ancienneMethode(),
                v.getTensionArterielle(),
                v.getPoids(),
                v.getDateProchainRdv()
        );
    }
}
