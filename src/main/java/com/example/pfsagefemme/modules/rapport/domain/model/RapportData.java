package com.example.pfsagefemme.modules.rapport.domain.model;

import com.example.pfsagefemme.modules.rapport.domain.model.entity.RapportMensuel;
import com.example.pfsagefemme.shared.domain.enums.AgeBracketEnum;
import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;

import java.util.List;
import java.util.Map;

/**
 * Vue agregee d'un rapport mensuel - rendue au frontend en une seule lecture.
 * L'entite {@link RapportMensuel} sous-jacente persiste l'en-tete + les lignes ;
 * cet enregistrement compose les sections lisibles directement.
 */
public record RapportData(
        RapportMensuel rapport,
        Stats stats,
        List<MethodeLine> section521,        // 5.2.1 Nouvelles
        List<MethodeLine> section521Bis,     // 5.2.1bis Anciennes (retours)
        List<MethodeLine> section522,        // 5.2.2 Actives
        Map<AgeBracketEnum, Long> section523,// 5.2.3 PVVIH actives PTME
        List<MethodeLine> section524,        // 5.2.4 Inactives
        List<MatriceCell> matriceChangements // matrice 5.2.1 DE -> VERS
) {

    /** Compteurs des 5 cards au-dessus du rapport. */
    public record Stats(long nouvellesPv, long anciennesPv, long actives, long inactives, long pvvihActives) {}

    /** Une ligne d'une section catalogue de methode (avec le total des 5 tranches). */
    public record MethodeLine(
            MethodeContraceptiveEnum methode,
            Map<AgeBracketEnum, Long> tranches,
            long total
    ) {}

    /** Une cellule non nulle de la matrice 5.2.1 DE -> VERS. */
    public record MatriceCell(
            MethodeContraceptiveEnum source,
            MethodeContraceptiveEnum cible,
            long nombre
    ) {}
}
