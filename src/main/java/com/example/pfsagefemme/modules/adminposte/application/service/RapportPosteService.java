package com.example.pfsagefemme.modules.adminposte.application.service;

import com.example.pfsagefemme.modules.adminposte.domain.port.in.GetRapportPosteUseCase;
import com.example.pfsagefemme.modules.patient.domain.port.out.PatienteRepository;
import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
import com.example.pfsagefemme.modules.postesante.domain.port.out.PosteSanteRepository;
import com.example.pfsagefemme.shared.domain.enums.AgeBracketEnum;
import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import com.example.pfsagefemme.shared.domain.enums.TypeRapportEnum;
import com.example.pfsagefemme.shared.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Rapport mensuel agrege pour un poste (chef de poste).
 * Meme logique que RapportNationalService mais scope a un seul poste.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RapportPosteService implements GetRapportPosteUseCase {

    private final PatienteRepository patienteRepository;
    private final PosteSanteRepository posteSanteRepository;

    @Override
    public RapportPosteData load(int mois, int annee, UUID posteSanteId) {
        PosteSante poste = posteSanteRepository.findById(posteSanteId)
                .orElseThrow(() -> new ResourceNotFoundException("Poste introuvable : " + posteSanteId));

        YearMonth ym = YearMonth.of(annee, mois);
        LocalDate from = ym.atDay(1);
        LocalDate to = ym.atEndOfMonth();
        YearMonth prev = ym.minusMonths(1);
        LocalDate prevFrom = prev.atDay(1);
        LocalDate prevTo = prev.atEndOfMonth();

        long nouvellesPv = patienteRepository.countNouvellesInscritesEntre(from, to, posteSanteId);
        long nouvellesPrev = patienteRepository.countNouvellesInscritesEntre(prevFrom, prevTo, posteSanteId);
        long total = patienteRepository.countAll(posteSanteId);
        long anciennesPv = total - nouvellesPv;
        long actives = patienteRepository.countByStatut(StatutPatienteEnum.ACTIVE, posteSanteId);
        long inactives = patienteRepository.countByStatut(StatutPatienteEnum.ARRETEE, posteSanteId)
                + patienteRepository.countByStatut(StatutPatienteEnum.ABANDON, posteSanteId)
                + patienteRepository.countByStatut(StatutPatienteEnum.INACTIVE, posteSanteId);
        long pvvih = patienteRepository.countPvvihActivesByAge(0, 200, posteSanteId);

        Double deltaNouv = delta(nouvellesPv, nouvellesPrev);

        List<MethodeLine> section521 = buildSection(TypeRapportEnum.NOUVELLES_PROGRAMME, from, to, posteSanteId);
        List<MethodeLine> section522 = buildSection(TypeRapportEnum.ACTIVES_METHODE, null, null, posteSanteId);
        List<MethodeLine> section524 = buildSection(TypeRapportEnum.INACTIVES_METHODE, from, to, posteSanteId);

        String periodeLabel = buildPeriodeLabel(mois, annee);

        return new RapportPosteData(
                mois, annee, periodeLabel,
                poste.getNom(),
                poste.getDistrict() != null ? poste.getDistrict() : "—",
                new KpiRapport(nouvellesPv, "Nvelles PV", deltaNouv),
                new KpiRapport(anciennesPv, "Anc. PV", null),
                new KpiRapport(actives, "Actives", null),
                new KpiRapport(inactives, "Inactives", null),
                new KpiRapport(pvvih, "PVVIH", null),
                section521,
                section522,
                section524
        );
    }

    private List<MethodeLine> buildSection(TypeRapportEnum type, LocalDate from, LocalDate to, UUID posteSanteId) {
        List<MethodeLine> lines = new ArrayList<>();
        for (MethodeContraceptiveEnum m : MethodeContraceptiveEnum.values()) {
            Map<AgeBracketEnum, Long> tranches = new EnumMap<>(AgeBracketEnum.class);
            long t10, t15, t20, t25, t50;
            if (type == TypeRapportEnum.NOUVELLES_PROGRAMME || type == TypeRapportEnum.INACTIVES_METHODE) {
                boolean isNouv = type == TypeRapportEnum.NOUVELLES_PROGRAMME;
                t10 = isNouv ? patienteRepository.countNouvellesByMethodeAndAge(m, 10, 14, from, to, posteSanteId)
                             : patienteRepository.countInactivesByMethodeAndAge(m, 10, 14, from, to, posteSanteId);
                t15 = isNouv ? patienteRepository.countNouvellesByMethodeAndAge(m, 15, 19, from, to, posteSanteId)
                             : patienteRepository.countInactivesByMethodeAndAge(m, 15, 19, from, to, posteSanteId);
                t20 = isNouv ? patienteRepository.countNouvellesByMethodeAndAge(m, 20, 24, from, to, posteSanteId)
                             : patienteRepository.countInactivesByMethodeAndAge(m, 20, 24, from, to, posteSanteId);
                t25 = isNouv ? patienteRepository.countNouvellesByMethodeAndAge(m, 25, 49, from, to, posteSanteId)
                             : patienteRepository.countInactivesByMethodeAndAge(m, 25, 49, from, to, posteSanteId);
                t50 = isNouv ? patienteRepository.countNouvellesByMethodeAndAge(m, 50, 200, from, to, posteSanteId)
                             : patienteRepository.countInactivesByMethodeAndAge(m, 50, 200, from, to, posteSanteId);
            } else {
                t10 = patienteRepository.countActivesByMethodeAndAge(m, 10, 14, posteSanteId);
                t15 = patienteRepository.countActivesByMethodeAndAge(m, 15, 19, posteSanteId);
                t20 = patienteRepository.countActivesByMethodeAndAge(m, 20, 24, posteSanteId);
                t25 = patienteRepository.countActivesByMethodeAndAge(m, 25, 49, posteSanteId);
                t50 = patienteRepository.countActivesByMethodeAndAge(m, 50, 200, posteSanteId);
            }
            long total = t10 + t15 + t20 + t25 + t50;
            if (total == 0) continue;

            tranches.put(AgeBracketEnum.BR_10_14, t10);
            tranches.put(AgeBracketEnum.BR_15_19, t15);
            tranches.put(AgeBracketEnum.BR_20_24, t20);
            tranches.put(AgeBracketEnum.BR_25_49, t25);
            tranches.put(AgeBracketEnum.BR_50_PLUS, t50);

            String label = m.getSousCategorie().isBlank()
                    ? m.getCategorie()
                    : m.getCategorie() + " (" + m.getSousCategorie() + ")";
            lines.add(new MethodeLine(m, label, tranches, total));
        }
        return lines;
    }

    private Double delta(long current, long previous) {
        if (previous == 0) return current == 0 ? 0.0 : null;
        return ((double) (current - previous) / previous) * 100.0;
    }

    private String buildPeriodeLabel(int mois, int annee) {
        String name = Month.of(mois).getDisplayName(TextStyle.FULL_STANDALONE, Locale.FRENCH);
        return name.substring(0, 1).toUpperCase() + name.substring(1) + " " + annee;
    }
}
