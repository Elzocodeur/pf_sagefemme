package com.example.pfsagefemme.modules.patient.application.service;

import com.example.pfsagefemme.modules.consultation.domain.port.out.FicheConsultationRepository;
import com.example.pfsagefemme.modules.consultation.domain.port.out.VisiteRepository;
import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.modules.patient.domain.port.in.GetPatientStatusDashboardUseCase;
import com.example.pfsagefemme.modules.patient.domain.port.in.ListPatientsByStatusUseCase;
import com.example.pfsagefemme.modules.patient.domain.port.in.UpdatePatientStatusUseCase;
import com.example.pfsagefemme.modules.patient.domain.port.out.PatienteRepository;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import com.example.pfsagefemme.shared.domain.exception.BusinessRuleException;
import com.example.pfsagefemme.shared.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Use cases du dashboard "Statuts patientes" :
 *   - compteurs (arretees, abandons, reprises)
 *   - listes (arretees, abandons calcules)
 *   - transitions (arreter, reprendre)
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PatientStatusService implements
        GetPatientStatusDashboardUseCase,
        ListPatientsByStatusUseCase,
        UpdatePatientStatusUseCase {

    private static final int DEFAULT_ABANDON_THRESHOLD_DAYS = 90;

    private final PatienteRepository patienteRepository;
    private final FicheConsultationRepository ficheConsultationRepository;
    private final VisiteRepository visiteRepository;

    // ====== Stats ============================================================

    @Override
    @Transactional(readOnly = true)
    public StatusStats stats(UUID posteSanteId) {
        long arretees = patienteRepository
                .findByStatutPatienteIn(List.of(StatutPatienteEnum.ARRETEE), posteSanteId).size();
        YearMonth ym = YearMonth.now();
        long arreteesCeMois = patienteRepository
                .findByStatutPatienteIn(List.of(StatutPatienteEnum.ARRETEE), posteSanteId).stream()
                .filter(p -> p.getDateArret() != null
                        && !p.getDateArret().isBefore(ym.atDay(1))
                        && !p.getDateArret().isAfter(ym.atEndOfMonth()))
                .count();
        long abandons = patienteRepository
                .findInactivesSinceThreshold(LocalDate.now().minusDays(DEFAULT_ABANDON_THRESHOLD_DAYS), posteSanteId)
                .size();
        long anciennesReprises = 0L; // TODO : suivi des reprises - necessite un audit log dedie
        return new StatusStats(arretees, arreteesCeMois, abandons, anciennesReprises);
    }

    // ====== Listings =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<StatusRow> arretees(UUID posteSanteId, String q, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateArret").and(Sort.by("nom")));
        String query = Optional.ofNullable(q).orElse("");
        Page<Patiente> patientPage = patienteRepository.findByStatutPatienteInPaged(
                List.of(StatutPatienteEnum.ARRETEE), posteSanteId, query, pageable);
        List<StatusRow> rows = patientPage.getContent().stream().map(this::enrich).toList();
        return new PageImpl<>(rows, pageable, patientPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StatusRow> abandons(UUID posteSanteId, int thresholdDays, String q, int page, int size) {
        int days = thresholdDays > 0 ? thresholdDays : DEFAULT_ABANDON_THRESHOLD_DAYS;
        PageRequest pageable = PageRequest.of(page, size, Sort.by("dateInscription"));
        String query = Optional.ofNullable(q).orElse("");
        Page<Patiente> patientPage = patienteRepository.findInactivesSinceThresholdPaged(
                LocalDate.now().minusDays(days), posteSanteId, query, pageable);
        List<StatusRow> rows = patientPage.getContent().stream().map(this::enrich).toList();
        return new PageImpl<>(rows, pageable, patientPage.getTotalElements());
    }

    private StatusRow enrich(Patiente p) {
        return new StatusRow(
                p,
                ficheConsultationRepository.findByPatienteId(p.getId()).orElse(null),
                visiteRepository.findLatestByPatienteId(p.getId()).orElse(null)
        );
    }

    // ====== Transitions ======================================================

    @Override
    public Patiente arreter(ArretCommand cmd) {
        Patiente patiente = loadOrThrow(cmd.patienteId());
        if (patiente.getStatutPatiente() == StatutPatienteEnum.ARRETEE) {
            return patiente; // idempotent
        }
        patiente.setStatutPatiente(StatutPatienteEnum.ARRETEE);
        patiente.setMotifArret(Optional.ofNullable(cmd.motif()).orElse("Non precise"));
        patiente.setDateArret(Optional.ofNullable(cmd.dateArret()).orElseGet(LocalDate::now));
        return patienteRepository.save(patiente);
    }

    @Override
    public Patiente reprendre(RepriseCommand cmd) {
        Patiente patiente = loadOrThrow(cmd.patienteId());
        if (patiente.getStatutPatiente() == StatutPatienteEnum.ACTIVE) {
            throw new BusinessRuleException("La patiente est deja active");
        }
        patiente.setStatutPatiente(StatutPatienteEnum.ACTIVE);
        patiente.setMotifArret(null);
        patiente.setDateArret(null);
        return patienteRepository.save(patiente);
    }

    private Patiente loadOrThrow(UUID id) {
        return patienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patiente introuvable : " + id));
    }
}
