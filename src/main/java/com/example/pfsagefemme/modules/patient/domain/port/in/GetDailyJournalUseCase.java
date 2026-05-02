package com.example.pfsagefemme.modules.patient.domain.port.in;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.FicheConsultation;
import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.shared.domain.enums.TypeVisiteEnum;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface GetDailyJournalUseCase {

    /** Filtres disponibles dans la barre de pills sous le calendrier. */
    enum Filter { ALL, NOUVELLE_PV, ANCIENNE_PV, ACTIVE, ARRETEE, ABANDON, PVVIH }

    /** Compteurs des 6 cards en haut du journal. */
    record DailyStats(
            long total,
            long nouvellesPv,
            long anciennesPv,
            long actives,
            long arretees,
            long abandons
    ) {}

    /** Une ligne du tableau "Liste des patientes consultees". */
    record JournalEntry(
            Visite visite,
            Patiente patiente,
            FicheConsultation fiche,
            TypeVisiteEnum typeVisite,
            long visiteCountIncludingThis
    ) {}

    record DailyJournal(
            LocalDate date,
            DailyStats stats,
            List<JournalEntry> entries
    ) {}

    DailyJournal forDate(LocalDate date, UUID posteSanteId, Filter filter);
}
