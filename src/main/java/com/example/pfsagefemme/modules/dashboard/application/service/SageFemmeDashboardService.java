package com.example.pfsagefemme.modules.dashboard.application.service;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.consultation.domain.port.out.VisiteRepository;
import com.example.pfsagefemme.modules.dashboard.domain.port.in.GetSageFemmeDashboardUseCase;
import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.modules.patient.domain.port.out.PatienteRepository;
import com.example.pfsagefemme.shared.domain.enums.StatutCliniqueEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Compose le dashboard sage-femme a partir des ports out des modules patient + consultation.
 *
 * Strategie :
 *  - 5 KPI cards : count actuel + delta % vs mois precedent
 *  - chart 7 jours : count visites par jour (lundi -> dimanche de la semaine en cours)
 *  - abandons a relancer : actives sans visite > 15 jours, top 3
 *  - actives recentes : top 3 actives triees par derniere visite
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SageFemmeDashboardService implements GetSageFemmeDashboardUseCase {

    private static final int ABANDON_RELANCE_THRESHOLD_DAYS = 15;
    private static final int ABANDON_RELANCE_LIMIT = 3;
    private static final int ACTIVES_RECENTES_LIMIT = 3;
    private static final int SUIVI_HEBDO_VISITES_THRESHOLD = 2;
    private static final int SUIVI_HEBDO_WINDOW_DAYS = 7;

    private final PatienteRepository patienteRepository;
    private final VisiteRepository visiteRepository;

    @Override
    public SageFemmeDashboard load(UUID posteSanteId) {
        return new SageFemmeDashboard(
                buildTotalCard(posteSanteId),
                buildActivesCard(posteSanteId),
                buildArreteesCard(posteSanteId),
                buildAbandonsCard(posteSanteId),
                buildPvvihCard(posteSanteId),
                buildActivity(posteSanteId),
                buildAbandonsRelance(posteSanteId),
                buildActivesRecentes(posteSanteId)
        );
    }

    // ====== KPI cards =========================================================

    private StatCard buildTotalCard(UUID posteSanteId) {
        long total = patienteRepository.countAll(posteSanteId);
        long currentMonth = patienteRepository.countNouvellesInscritesEntre(
                YearMonth.now().atDay(1), YearMonth.now().atEndOfMonth(), posteSanteId);
        long previousMonth = patienteRepository.countNouvellesInscritesEntre(
                YearMonth.now().minusMonths(1).atDay(1),
                YearMonth.now().minusMonths(1).atEndOfMonth(),
                posteSanteId);
        Double delta = computeDeltaPercent(currentMonth, previousMonth);
        return new StatCard(total, delta, formatDelta(delta), trendFromDelta(delta));
    }

    private StatCard buildActivesCard(UUID posteSanteId) {
        long actives = patienteRepository.countByStatut(StatutPatienteEnum.ACTIVE, posteSanteId);
        long currentMonth = patienteRepository.countNouvellesInscritesEntre(
                YearMonth.now().atDay(1), YearMonth.now().atEndOfMonth(), posteSanteId);
        long previousMonth = patienteRepository.countNouvellesInscritesEntre(
                YearMonth.now().minusMonths(1).atDay(1),
                YearMonth.now().minusMonths(1).atEndOfMonth(),
                posteSanteId);
        Double delta = computeDeltaPercent(currentMonth, previousMonth);
        return new StatCard(actives, delta, formatDelta(delta), trendFromDelta(delta));
    }

    private StatCard buildArreteesCard(UUID posteSanteId) {
        long arretees = patienteRepository.countByStatut(StatutPatienteEnum.ARRETEE, posteSanteId);
        long currentMonth = patienteRepository.countArreteesEntre(
                YearMonth.now().atDay(1), YearMonth.now().atEndOfMonth(), posteSanteId);
        long previousMonth = patienteRepository.countArreteesEntre(
                YearMonth.now().minusMonths(1).atDay(1),
                YearMonth.now().minusMonths(1).atEndOfMonth(),
                posteSanteId);
        Double delta = computeDeltaPercent(currentMonth, previousMonth);
        return new StatCard(arretees, delta, formatDelta(delta), trendFromDelta(delta));
    }

    private StatCard buildAbandonsCard(UUID posteSanteId) {
        long abandons = patienteRepository
                .findInactivesSinceThreshold(LocalDate.now().minusDays(90), posteSanteId).size();
        // Tendance "ALERT" affichee dans tous les cas - calcul fin a faire avec un audit log.
        return new StatCard(abandons, null, "Alerte hausse", Trend.ALERT);
    }

    private StatCard buildPvvihCard(UUID posteSanteId) {
        long pvvih = patienteRepository.countPvvih(posteSanteId);
        return new StatCard(pvvih, null, "Suivi prioritaire", Trend.PRIORITY);
    }

    // ====== Chart activite 7 jours ===========================================

    private Activity buildActivity(UUID posteSanteId) {
        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
        List<ActivityDay> days = new ArrayList<>(7);
        long total = 0;
        for (int i = 0; i < 7; i++) {
            LocalDate day = monday.plusDays(i);
            long count = visiteRepository.countByDateAndPosteSanteId(day, posteSanteId);
            String label = day.getDayOfWeek()
                    .getDisplayName(TextStyle.SHORT_STANDALONE, Locale.FRENCH);
            label = label.substring(0, 1).toUpperCase() + label.substring(1, Math.min(3, label.length()));
            days.add(new ActivityDay(day, label, count));
            total += count;
        }
        return new Activity(total, days);
    }

    // ====== Abandons a relancer ==============================================

    private AbandonsRelance buildAbandonsRelance(UUID posteSanteId) {
        LocalDate threshold = LocalDate.now().minusDays(ABANDON_RELANCE_THRESHOLD_DAYS);
        List<Patiente> candidates = patienteRepository.findInactivesSinceThreshold(threshold, posteSanteId);

        List<AbandonRelance> top = candidates.stream()
                .map(p -> {
                    Visite last = visiteRepository.findLatestByPatienteId(p.getId()).orElse(null);
                    LocalDate dernierContact = last != null
                            ? last.getDate()
                            : (p.getDateInscription() != null ? p.getDateInscription() : LocalDate.now());
                    long jours = ChronoUnit.DAYS.between(dernierContact, LocalDate.now());
                    return new AbandonRelance(p, last, jours);
                })
                .sorted((a, b) -> Long.compare(b.joursSansContact(), a.joursSansContact()))
                .limit(ABANDON_RELANCE_LIMIT)
                .toList();

        return new AbandonsRelance(ABANDON_RELANCE_THRESHOLD_DAYS, top, candidates.size());
    }

    // ====== Actives recentes =================================================

    private List<ActiveRecente> buildActivesRecentes(UUID posteSanteId) {
        return patienteRepository.findActivesOrderByLastVisitDesc(posteSanteId, ACTIVES_RECENTES_LIMIT).stream()
                .map(p -> {
                    Visite last = visiteRepository.findLatestByPatienteId(p.getId()).orElse(null);
                    StatutCliniqueEnum statut = computeStatutClinique(p);
                    return new ActiveRecente(p, last, statut);
                })
                .toList();
    }

    private StatutCliniqueEnum computeStatutClinique(Patiente patiente) {
        if (Boolean.TRUE.equals(patiente.getPvvih())) return StatutCliniqueEnum.SUIVI_HEBDO;
        long visitesRecentes = visiteRepository.countByPatienteIdAndDateGreaterThanEqual(
                patiente.getId(), LocalDate.now().minusDays(SUIVI_HEBDO_WINDOW_DAYS));
        if (visitesRecentes >= SUIVI_HEBDO_VISITES_THRESHOLD) return StatutCliniqueEnum.SUIVI_HEBDO;
        return StatutCliniqueEnum.STABLE;
    }

    // ====== Helpers ==========================================================

    private Double computeDeltaPercent(long current, long previous) {
        if (previous == 0) return current == 0 ? 0.0 : null;
        return ((double) (current - previous) / previous) * 100.0;
    }

    private String formatDelta(Double delta) {
        if (delta == null) return "-";
        String sign = delta >= 0 ? "+" : "";
        return String.format("%s%.0f%% ce mois", sign, delta);
    }

    private Trend trendFromDelta(Double delta) {
        if (delta == null) return Trend.STABLE;
        if (delta > 0) return Trend.UP;
        if (delta < 0) return Trend.DOWN;
        return Trend.STABLE;
    }
}
