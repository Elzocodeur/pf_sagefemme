package com.example.pfsagefemme.modules.dashboard.application.service;

import com.example.pfsagefemme.modules.dashboard.domain.port.in.GetNationalStatsUseCase;
import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.modules.patient.domain.port.out.PatienteRepository;
import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
import com.example.pfsagefemme.modules.postesante.domain.port.out.PosteSanteRepository;
import com.example.pfsagefemme.modules.user.domain.port.out.UtilisateurRepository;
import com.example.pfsagefemme.shared.domain.enums.RoleEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * Statistiques nationales (vue SUPER_ADMIN) :
 *  - 5 cards globales (Total Patientes, Actives, Abandons, PVVIH, Sages-femmes)
 *  - Tableau "Vue par poste de sante" : 1 ligne par poste, avec compteurs et taux d'activite
 *  - Total national (ligne de pied de tableau)
 *  - Dernieres patientes enregistrees (toutes regions)
 *
 * Reutilise les ports out de patient/postesante/user - aucune lecture directe de la BDD.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NationalStatsService implements GetNationalStatsUseCase {

    private static final int RECENT_PATIENTS_LIMIT = 10;
    private static final int ABANDON_THRESHOLD_DAYS = 90;

    private final PatienteRepository patienteRepository;
    private final PosteSanteRepository posteSanteRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Override
    public NationalStats load() {
        long totalPatientes = patienteRepository.countAll(null);
        long actives = patienteRepository.countByStatut(StatutPatienteEnum.ACTIVE, null);
        long abandons = patienteRepository
                .findInactivesSinceThreshold(LocalDate.now().minusDays(ABANDON_THRESHOLD_DAYS), null).size();
        long pvvih = patienteRepository.countPvvih(null);
        long sagesFemmes = utilisateurRepository.findByRole(RoleEnum.SAGE_FEMME).size();

        GlobalStats global = new GlobalStats(totalPatientes, actives, abandons, pvvih, sagesFemmes);

        List<PosteRow> rows = posteSanteRepository.findAll().stream()
                .map(this::buildRow)
                .sorted(Comparator.comparing(r -> r.poste().getNom()))
                .toList();

        long totalNational = rows.stream().mapToLong(PosteRow::total).sum();
        int tauxMoyen = rows.isEmpty()
                ? 0
                : (int) Math.round(rows.stream().mapToInt(PosteRow::tauxActivitePercent).average().orElse(0));

        List<Patiente> recents = patienteRepository.findAll().stream()
                .filter(p -> p.getDateInscription() != null)
                .sorted(Comparator.comparing(Patiente::getDateInscription).reversed())
                .limit(RECENT_PATIENTS_LIMIT)
                .toList();

        return new NationalStats(global, rows, totalNational, tauxMoyen, recents);
    }

    private PosteRow buildRow(PosteSante p) {
        long total = patienteRepository.countAll(p.getId());
        long actives = patienteRepository.countByStatut(StatutPatienteEnum.ACTIVE, p.getId());
        long abandons = patienteRepository
                .findInactivesSinceThreshold(LocalDate.now().minusDays(ABANDON_THRESHOLD_DAYS), p.getId()).size();
        long pvvih = patienteRepository.countPvvih(p.getId());
        long sf = utilisateurRepository.findSageFemmesByPosteSanteId(p.getId()).size();
        int taux = total == 0 ? 0 : (int) Math.round((actives * 100.0) / total);
        return new PosteRow(p, total, actives, abandons, pvvih, sf, taux);
    }
}
