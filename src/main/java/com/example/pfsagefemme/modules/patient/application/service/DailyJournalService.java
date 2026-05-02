package com.example.pfsagefemme.modules.patient.application.service;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.consultation.domain.port.out.FicheConsultationRepository;
import com.example.pfsagefemme.modules.consultation.domain.port.out.VisiteRepository;
import com.example.pfsagefemme.modules.consultation.infrastructure.persistence.VisiteJpaRepository;
import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.modules.patient.domain.port.in.GetDailyJournalUseCase;
import com.example.pfsagefemme.modules.patient.domain.port.out.PatienteRepository;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import com.example.pfsagefemme.shared.domain.enums.TypeVisiteEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

/**
 * Use case "Journal du jour".
 *
 * Construit en deux temps :
 *  1. agrege les compteurs (cards) - une serie de comptages
 *  2. construit la liste des entrees a partir des visites du jour, en classifiant
 *     chaque ligne (NOUVELLE_PV / ANCIENNE_PV / SUIVI) selon l'historique de la patiente
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DailyJournalService implements GetDailyJournalUseCase {

    private static final int ABANDON_THRESHOLD_DAYS = 90;

    private final PatienteRepository patienteRepository;
    private final VisiteRepository visiteRepository;
    private final FicheConsultationRepository ficheConsultationRepository;
    /* compteur de visites par patiente jusqu'a une date - lecture seule directe sur la JPA repo */
    private final VisiteJpaRepository visiteJpaRepository;

    @Override
    public DailyJournal forDate(LocalDate date, UUID posteSanteId, Filter filter) {
        DailyStats stats = computeStats(date, posteSanteId);
        List<Visite> visites = visiteRepository.findByDateAndPosteSanteId(date, posteSanteId);

        List<JournalEntry> entries = visites.stream()
                .map(v -> buildEntry(v, date))
                .filter(e -> matchesFilter(e, filter == null ? Filter.ALL : filter))
                .toList();

        return new DailyJournal(date, stats, entries);
    }

    private DailyStats computeStats(LocalDate date, UUID posteSanteId) {
        List<Visite> visites = visiteRepository.findByDateAndPosteSanteId(date, posteSanteId);
        long total = visites.size();

        long nouvelles = patienteRepository.countNouvellesOnDate(date, posteSanteId);
        long anciennes = visites.stream()
                .filter(v -> {
                    Patiente p = v.getPatiente();
                    return p != null && p.getDateInscription() != null && !p.getDateInscription().equals(date);
                })
                .count();
        long actives = visites.stream()
                .filter(v -> v.getPatiente() != null && v.getPatiente().getStatutPatiente() == StatutPatienteEnum.ACTIVE)
                .count();
        long arretees = patienteRepository.countArreteesOnDate(date, posteSanteId);
        long abandons = patienteRepository
                .findInactivesSinceThreshold(date.minusDays(ABANDON_THRESHOLD_DAYS), posteSanteId)
                .size();
        return new DailyStats(total, nouvelles, anciennes, actives, arretees, abandons);
    }

    private JournalEntry buildEntry(Visite visite, LocalDate journalDate) {
        Patiente p = visite.getPatiente();
        long visiteCount = visiteJpaRepository.countByPatienteIdAndDateLessThanEqual(p.getId(), journalDate);
        TypeVisiteEnum type = computeType(p, journalDate, visiteCount);
        return new JournalEntry(
                visite,
                p,
                ficheConsultationRepository.findByPatienteId(p.getId()).orElse(null),
                type,
                visiteCount
        );
    }

    private TypeVisiteEnum computeType(Patiente patiente, LocalDate journalDate, long visiteCount) {
        // Premiere visite jamais : NOUVELLE_PV
        if (visiteCount <= 1) return TypeVisiteEnum.NOUVELLE_PV;
        // Patiente inscrite avant le mois courant : ANCIENNE_PV
        YearMonth currentMonth = YearMonth.from(journalDate);
        if (patiente.getDateInscription() != null
                && YearMonth.from(patiente.getDateInscription()).isBefore(currentMonth)) {
            return TypeVisiteEnum.ANCIENNE_PV;
        }
        return TypeVisiteEnum.SUIVI;
    }

    private boolean matchesFilter(JournalEntry entry, Filter filter) {
        Patiente p = entry.patiente();
        return switch (filter) {
            case ALL -> true;
            case NOUVELLE_PV -> entry.typeVisite() == TypeVisiteEnum.NOUVELLE_PV;
            case ANCIENNE_PV -> entry.typeVisite() == TypeVisiteEnum.ANCIENNE_PV;
            case ACTIVE -> p.getStatutPatiente() == StatutPatienteEnum.ACTIVE;
            case ARRETEE -> p.getStatutPatiente() == StatutPatienteEnum.ARRETEE;
            case ABANDON -> p.getStatutPatiente() == StatutPatienteEnum.ABANDON;
            case PVVIH -> Boolean.TRUE.equals(p.getPvvih());
        };
    }
}
