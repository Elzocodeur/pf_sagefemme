package com.example.pfsagefemme.modules.patient.domain.model;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.FicheConsultation;
import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;

import java.util.List;

/**
 * Vue agregee : patiente + fiche de consultation + visites.
 * Sert de retour des use cases qui lisent l'integralite du dossier.
 */
public record PatientFolder(
        Patiente patiente,
        FicheConsultation ficheConsultation,
        List<Visite> visites
) {}
