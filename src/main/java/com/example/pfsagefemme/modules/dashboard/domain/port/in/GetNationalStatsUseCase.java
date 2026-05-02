package com.example.pfsagefemme.modules.dashboard.domain.port.in;

import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;

import java.util.List;

public interface GetNationalStatsUseCase {

    /** Compteurs des 5 cards en haut de l'ecran "Statistiques nationales". */
    record GlobalStats(long totalPatientes, long actives, long abandons, long pvvih, long sagesFemmes) {}

    /** Une ligne du tableau "Vue par poste de sante". */
    record PosteRow(
            PosteSante poste,
            long total,
            long actives,
            long abandons,
            long pvvih,
            long sagesFemmes,
            int tauxActivitePercent
    ) {}

    record NationalStats(
            GlobalStats global,
            List<PosteRow> postesRows,
            long totalNational,
            int tauxMoyenPercent,
            List<Patiente> recentPatients
    ) {}

    NationalStats load();
}
