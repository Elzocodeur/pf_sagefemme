package com.example.pfsagefemme.modules.patient.domain.port.in;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.FicheConsultation;
import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;

import java.util.List;
import java.util.UUID;

public interface ListPatientsByStatusUseCase {

    record StatusRow(Patiente patiente, FicheConsultation fiche, Visite latestVisite) {}

    List<StatusRow> arretees(UUID posteSanteId);

    /**
     * Patientes ACTIVE sans visite depuis {@code thresholdDays} jours - candidates "abandon".
     * Defaut recommande : 90 jours.
     */
    List<StatusRow> abandons(UUID posteSanteId, int thresholdDays);
}
