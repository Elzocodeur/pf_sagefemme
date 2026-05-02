package com.example.pfsagefemme.modules.patient.application.service;

import com.example.pfsagefemme.modules.consultation.domain.port.out.FicheConsultationRepository;
import com.example.pfsagefemme.modules.consultation.domain.port.out.VisiteRepository;
import com.example.pfsagefemme.modules.patient.domain.port.in.GetPvvihDashboardUseCase;
import com.example.pfsagefemme.modules.patient.domain.port.in.ListPvvihPatientsUseCase;
import com.example.pfsagefemme.modules.patient.domain.port.out.PatienteRepository;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

/**
 * Use cases du dashboard "Femmes PVVIH" (Section 5.2.3 du Rapport Mensuel).
 *
 *  - {@link GetPvvihDashboardUseCase}  : compteurs (cumul annuel / actives / nouvelles ce mois)
 *  - {@link ListPvvihPatientsUseCase}  : liste enrichie (patiente + fiche + derniere visite)
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PvvihService implements GetPvvihDashboardUseCase, ListPvvihPatientsUseCase {

    private final PatienteRepository patienteRepository;
    private final FicheConsultationRepository ficheConsultationRepository;
    private final VisiteRepository visiteRepository;

    @Override
    public PvvihStats stats(UUID posteSanteId) {
        long total = patienteRepository.countByPvvihTrue(posteSanteId);
        long actives = patienteRepository.countByPvvihTrueAndStatutPatiente(StatutPatienteEnum.ACTIVE, posteSanteId);
        YearMonth ym = YearMonth.now();
        long nouvellesCeMois = patienteRepository.countByPvvihTrueAndDateInscriptionBetween(
                ym.atDay(1), ym.atEndOfMonth(), posteSanteId
        );
        return new PvvihStats(total, actives, nouvellesCeMois);
    }

    @Override
    public List<PvvihRow> list(UUID posteSanteId) {
        return patienteRepository.findPvvih(posteSanteId).stream()
                .map(p -> new PvvihRow(
                        p,
                        ficheConsultationRepository.findByPatienteId(p.getId()).orElse(null),
                        visiteRepository.findLatestByPatienteId(p.getId()).orElse(null)
                ))
                .toList();
    }
}
