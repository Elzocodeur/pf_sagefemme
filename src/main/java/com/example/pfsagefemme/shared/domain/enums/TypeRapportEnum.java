package com.example.pfsagefemme.shared.domain.enums;

/**
 * Type d'une ligne de rapport - identifie le tableau d'origine.
 * Les 3 tableaux 5.2.1 / 5.2.2 / 5.2.4 partagent la meme structure
 * (methode + 5 tranches d'age + total).
 */
public enum TypeRapportEnum {
    /** Tableau 5.2.1. */
    NOUVELLES_PROGRAMME,
    /** Tableau 5.2.2. */
    ACTIVES_METHODE,
    /** Tableau 5.2.4. */
    INACTIVES_METHODE
}
