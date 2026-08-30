package com.example.pfsagefemme.modules.patient.domain.port.in;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.FicheConsultation;
import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ListPatientsByStatusUseCase {

  record StatusRow(Patiente patiente, FicheConsultation fiche, Visite latestVisite) {}

  Page<StatusRow> arretees(UUID posteSanteId, String q, int page, int size);

<<<<<<< Updated upstream
  Page<StatusRow> abandons(UUID posteSanteId, int thresholdDays, String q, int page, int size);
=======

    List<StatusRow> abandons(UUID posteSanteId, int thresholdDays);
>>>>>>> Stashed changes
}
