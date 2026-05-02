package com.example.pfsagefemme.modules.patient.infrastructure.rest.mapper;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.FicheConsultation;
import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.patient.domain.port.in.ListPatientsByStatusUseCase;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.PatientStatusRowResponse;

public final class PatientStatusRowMapper {

    private PatientStatusRowMapper() {}

    public static PatientStatusRowResponse toResponse(ListPatientsByStatusUseCase.StatusRow row) {
        var p = row.patiente();
        FicheConsultation fiche = row.fiche();
        Visite latest = row.latestVisite();
        return new PatientStatusRowResponse(
                p.getId(),
                p.getNumeroDossier(),
                p.getPrenom(),
                p.getNom(),
                p.getAdresse(),
                p.getStatutPatiente(),
                fiche == null ? null : fiche.getMethodeChoisie(),
                p.getDateArret(),
                p.getMotifArret(),
                latest == null ? null : latest.getDateProchainRdv()
        );
    }
}
