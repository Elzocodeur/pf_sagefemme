package com.example.pfsagefemme.modules.patient.domain.port.in;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.FicheConsultation;
import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;

import java.util.UUID;

public interface ListPvvihPatientsUseCase {

    /** Vue agregee : patiente + sa fiche + sa derniere visite (pour le badge "Statut PF"). */
    record PvvihRow(Patiente patiente, FicheConsultation fiche, Visite latestVisite) {}

    java.util.List<PvvihRow> list(UUID posteSanteId);
}
