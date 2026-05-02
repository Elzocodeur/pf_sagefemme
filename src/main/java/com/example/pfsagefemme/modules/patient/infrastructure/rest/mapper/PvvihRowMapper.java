package com.example.pfsagefemme.modules.patient.infrastructure.rest.mapper;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.FicheConsultation;
import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.patient.domain.port.in.ListPvvihPatientsUseCase;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.PvvihRowResponse;
import com.example.pfsagefemme.shared.domain.enums.StatutSuiviPfEnum;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class PvvihRowMapper {

    /** Seuil au-dela duquel un RDV non encore arrive est encore "a jour" (en jours). */
    private static final long RENOUVELLEMENT_WINDOW_DAYS = 7;

    private PvvihRowMapper() {}

    public static PvvihRowResponse toResponse(ListPvvihPatientsUseCase.PvvihRow row) {
        var p = row.patiente();
        FicheConsultation fiche = row.fiche();
        Visite latest = row.latestVisite();

        LocalDate prochainRdv = latest == null ? null : latest.getDateProchainRdv();

        return new PvvihRowResponse(
                p.getId(),
                p.getNumeroDossier(),
                p.getPrenom(),
                p.getNom(),
                p.getAge(),
                p.getStatutPatiente(),
                p.getStadeOms(),
                p.getTraitementArv(),
                fiche == null ? null : fiche.getMethodeChoisie(),
                computeBadge(fiche, prochainRdv),
                prochainRdv
        );
    }

    /**
     * Calcule le badge "Statut PF" affiche dans l'UI :
     *   URGENT          : pas de methode OU prochain RDV depasse
     *   RENOUVELLEMENT  : prochain RDV dans les {@value #RENOUVELLEMENT_WINDOW_DAYS} jours
     *   A_JOUR          : prochain RDV au-dela
     */
    private static StatutSuiviPfEnum computeBadge(FicheConsultation fiche, LocalDate prochainRdv) {
        if (fiche == null || fiche.getMethodeChoisie() == null) return StatutSuiviPfEnum.URGENT;
        if (prochainRdv == null) return StatutSuiviPfEnum.URGENT;
        LocalDate today = LocalDate.now();
        if (prochainRdv.isBefore(today)) return StatutSuiviPfEnum.URGENT;
        long daysUntil = ChronoUnit.DAYS.between(today, prochainRdv);
        if (daysUntil <= RENOUVELLEMENT_WINDOW_DAYS) return StatutSuiviPfEnum.RENOUVELLEMENT;
        return StatutSuiviPfEnum.A_JOUR;
    }
}
