package com.example.pfsagefemme.modules.patient.application.service;

import com.example.pfsagefemme.modules.consultation.domain.port.out.FicheConsultationRepository;
import com.example.pfsagefemme.modules.consultation.domain.port.out.VisiteRepository;
import com.example.pfsagefemme.modules.patient.domain.port.in.GetPvvihDashboardUseCase;
import com.example.pfsagefemme.modules.patient.domain.port.in.ListPvvihPatientsUseCase;
import com.example.pfsagefemme.modules.patient.domain.port.out.PatienteRepository;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
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
        ym.atDay(1), ym.atEndOfMonth(), posteSanteId);
    return new PvvihStats(total, actives, nouvellesCeMois);
  }

  @Override
  public Page<PvvihRow> list(UUID posteSanteId, String q, int page, int size) {
    PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateInscription").and(Sort.by("nom")));
    String query = Optional.ofNullable(q).orElse("");
    Page<com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente> patientPage =
        patienteRepository.findPvvihPaged(posteSanteId, query, pageable);
    List<PvvihRow> rows = patientPage.getContent().stream()
        .map(p -> new PvvihRow(
            p,
            ficheConsultationRepository.findByPatienteId(p.getId()).orElse(null),
            visiteRepository.findLatestByPatienteId(p.getId()).orElse(null)
        ))
        .toList();
    return new PageImpl<>(rows, pageable, patientPage.getTotalElements());
  }
}
