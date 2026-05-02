package com.example.pfsagefemme.modules.rapport.domain.model;

import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;

import java.util.List;

/**
 * Vue agregee du Rapport National PF (somme tous postes pour un mois).
 *
 * Compose :
 *  - 5 cards globales (Nvelles PV, Anc. PV, Actives, Inactives, PVVIH actives) + delta % vs mois precedent
 *  - tableau recapitulatif par poste (Section 5.2)
 *  - detail des methodes longue duree (LARC) : DIU + Implants
 *  - consommation des intrants hormonaux : Pilules + Injectables
 */
public record RapportNationalData(
        int mois,
        int annee,
        Stats stats,
        List<PosteRow> postesRows,
        TotalRow totalNational,
        List<MethodeQuantite> larcDetail,
        List<MethodeQuantite> hormonauxConsommes
) {

    public record Stats(
            long nouvellesPv, Double deltaNouvellesPercent,
            long anciennesPv,
            long actives,
            long inactives,
            long pvvihActives
    ) {}

    public record PosteRow(
            PosteSante poste,
            long nouvellesPv,
            long anciennesPv,
            long actives,
            long inactives,
            long pvvih,
            long total
    ) {}

    public record TotalRow(long nouvellesPv, long anciennesPv, long actives, long inactives, long pvvih, long total) {}

    public record MethodeQuantite(MethodeContraceptiveEnum methode, String label, long quantite) {}
}
