package com.example.pfsagefemme.modules.patient.domain.port.in;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.FicheConsultation;
import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ListPvvihPatientsUseCase {

  record PvvihRow(Patiente patiente, FicheConsultation fiche, Visite latestVisite) {}

  Page<PvvihRow> list(UUID posteSanteId, String q, int page, int size);
}
