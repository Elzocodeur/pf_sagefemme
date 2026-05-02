package com.example.pfsagefemme.modules.postesante.domain.port.in;

import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;

import java.util.List;

public interface GetPostesSanteDashboardUseCase {

    /** Compteurs des 4 cards en haut de l'ecran "Postes de sante". */
    record PostesStats(long postesActifs, long totalPatientes, long totalSagesFemmes, long districtsCouverts) {}

    /** Ligne enrichie du tableau (poste + nombre de patientes et SF). */
    record PosteSanteRow(PosteSante poste, long nombrePatientes, long nombreSagesFemmes) {}

    PostesStats stats();

    /**
     * @param actif filtre Tous (null), Actifs (true), Inactifs (false)
     */
    List<PosteSanteRow> list(Boolean actif);
}
