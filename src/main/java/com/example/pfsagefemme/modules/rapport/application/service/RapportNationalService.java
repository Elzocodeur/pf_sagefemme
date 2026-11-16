package com.example.pfsagefemme.modules.rapport.application.service;

import com.example.pfsagefemme.modules.patient.domain.port.out.PatienteRepository;
import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
import com.example.pfsagefemme.modules.postesante.domain.port.out.PosteSanteRepository;
import com.example.pfsagefemme.modules.rapport.domain.model.RapportNationalData;
import com.example.pfsagefemme.modules.rapport.domain.port.in.GetRapportNationalUseCase;
import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Rapport National PF - agregat tous postes pour un mois donne.
 *
 * Calculs :
 *  - Nouvelles PV : patientes inscrites pendant le mois
 *  - Anciennes PV : patientes inscrites avant le mois et revues pendant le mois
 *  - Actives / Inactives : snapshot par statut
 *  - PVVIH actives : snapshot
 *  - Detail LARC : count des patientes ayant DIU + Implants comme methode courante
 *  - Consommation hormonaux : count des patientes ayant Pilules + Injectables comme methode courante
 *
 * Tous les comptes sont restreints au scope national (poste = null).
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RapportNationalService implements GetRapportNationalUseCase {

    /** Tranche d'age 0-200 = "tous ages" pour les comptages globaux. */
    private static final int ALL_AGES_MIN = 0;
    private static final int ALL_AGES_MAX = 200;

    private static final List<MethodeContraceptiveEnum> LARC = List.of(
            MethodeContraceptiveEnum.DIU_TCU_380A,
            MethodeContraceptiveEnum.DIU_TCU_375_SLEEK,
            MethodeContraceptiveEnum.DIU_POST_PARTUM,
            MethodeContraceptiveEnum.IMPLANT_1_CAPSULE,
            MethodeContraceptiveEnum.IMPLANT_2_CAPSULES
    );

    private static final List<MethodeContraceptiveEnum> HORMONAUX = List.of(
            MethodeContraceptiveEnum.PILULE_PP,
            MethodeContraceptiveEnum.PILULE_POP,
            MethodeContraceptiveEnum.INJECTABLE_IM,
            MethodeContraceptiveEnum.INJECTABLE_SC_PRESTATAIRE,
            MethodeContraceptiveEnum.INJECTABLE_SC_AUTO_INJECTION
    );

    private final PatienteRepository patienteRepository;
    private final PosteSanteRepository posteSanteRepository;

    @Override
    public RapportNationalData load(int mois, int annee) {
        YearMonth ym = YearMonth.of(annee, mois);
        LocalDate from = ym.atDay(1);
        LocalDate to = ym.atEndOfMonth();
        YearMonth previous = ym.minusMonths(1);
        LocalDate prevFrom = previous.atDay(1);
        LocalDate prevTo = previous.atEndOfMonth();

        long nouvellesPv = patienteRepository.countNouvellesInscritesEntre(from, to, null);
        long nouvellesPvPrev = patienteRepository.countNouvellesInscritesEntre(prevFrom, prevTo, null);
        Double delta = computeDeltaPercent(nouvellesPv, nouvellesPvPrev);

        long anciennesPv = patienteRepository.countAnciennesPv(from, to, null, null);
        long actives = patienteRepository.countByStatut(StatutPatienteEnum.ACTIVE, null);
        long inactives = countInactives(from, to, null);
        long pvvihActives = patienteRepository.countPvvihActivesByAge(ALL_AGES_MIN, ALL_AGES_MAX, null);

        var stats = new RapportNationalData.Stats(
                nouvellesPv, delta, anciennesPv, actives, inactives, pvvihActives
        );

        // Tableau par poste
        List<RapportNationalData.PosteRow> rows = posteSanteRepository.findAll().stream()
                .map(p -> buildPosteRow(p, from, to))
                .toList();

        long totalNouvelles = rows.stream().mapToLong(RapportNationalData.PosteRow::nouvellesPv).sum();
        long totalAnciennes = rows.stream().mapToLong(RapportNationalData.PosteRow::anciennesPv).sum();
        long totalActives = rows.stream().mapToLong(RapportNationalData.PosteRow::actives).sum();
        long totalInactives = rows.stream().mapToLong(RapportNationalData.PosteRow::inactives).sum();
        long totalPvvih = rows.stream().mapToLong(RapportNationalData.PosteRow::pvvih).sum();
        long totalGlobal = rows.stream().mapToLong(RapportNationalData.PosteRow::total).sum();

        var totalRow = new RapportNationalData.TotalRow(
                totalNouvelles, totalAnciennes, totalActives, totalInactives, totalPvvih, totalGlobal
        );

        // Detail LARC + Hormonaux (snapshot national, methode = patientes ACTIVE avec cette methode courante)
        List<RapportNationalData.MethodeQuantite> larc = LARC.stream()
                .map(m -> new RapportNationalData.MethodeQuantite(
                        m, m.getCategorie() + " - " + m.getSousCategorie(),
                        patienteRepository.countActivesByMethodeAndAge(m, ALL_AGES_MIN, ALL_AGES_MAX, null)))
                .toList();

        List<RapportNationalData.MethodeQuantite> hormonaux = HORMONAUX.stream()
                .map(m -> new RapportNationalData.MethodeQuantite(
                        m, m.getCategorie() + " - " + m.getSousCategorie(),
                        patienteRepository.countActivesByMethodeAndAge(m, ALL_AGES_MIN, ALL_AGES_MAX, null)))
                .toList();

        return new RapportNationalData(mois, annee, stats, rows, totalRow, larc, hormonaux);
    }

    private RapportNationalData.PosteRow buildPosteRow(PosteSante poste, LocalDate from, LocalDate to) {
        long nouvelles = patienteRepository.countNouvellesInscritesEntre(from, to, poste.getId());
        long total = patienteRepository.countAll(poste.getId());
        long anciennes = patienteRepository.countAnciennesPv(from, to, poste.getId(), null);
        long actives = patienteRepository.countByStatut(StatutPatienteEnum.ACTIVE, poste.getId());
        long inactives = countInactives(from, to, poste.getId());
        long pvvih = patienteRepository.countPvvihActivesByAge(ALL_AGES_MIN, ALL_AGES_MAX, poste.getId());
        return new RapportNationalData.PosteRow(poste, nouvelles, anciennes, actives, inactives, pvvih, total);
    }

    /** Meme definition que le tableau 5.2.4 du rapport mensuel (arretees du mois + inactives + abandons). */
    private long countInactives(LocalDate from, LocalDate to, java.util.UUID posteSanteId) {
        long n = 0;
        for (MethodeContraceptiveEnum m : MethodeContraceptiveEnum.values()) {
            n += patienteRepository.countInactivesByMethodeAndAge(m, ALL_AGES_MIN, ALL_AGES_MAX, from, to, posteSanteId);
        }
        return n;
    }

    private Double computeDeltaPercent(long current, long previous) {
        if (previous == 0) return current == 0 ? 0.0 : null;
        return ((double) (current - previous) / previous) * 100.0;
    }
}
