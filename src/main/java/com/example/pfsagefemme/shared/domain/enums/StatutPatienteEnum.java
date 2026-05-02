package com.example.pfsagefemme.shared.domain.enums;

/**
 * Statut d'une patiente dans le programme de planification familiale.
 * Determine dans quel tableau du rapport mensuel elle est comptabilisee.
 *
 * Pour le rapport mensuel :
 *   - NOUVELLE                  -> Tableau 5.2.1
 *   - ACTIVE                    -> Tableau 5.2.2
 *   - ARRETEE / ABANDON / INACTIVE -> Tableau 5.2.4
 */
public enum StatutPatienteEnum {
    /** Tableau 5.2.1 - Nouvelles dans le programme. */
    NOUVELLE,
    /** Tableau 5.2.2 - Actives selon la methode. */
    ACTIVE,
    /** Patiente ayant volontairement arrete sa contraception (motif renseigne). */
    ARRETEE,
    /** Patiente perdue de vue (RDV manque, plus de visite depuis longtemps). */
    ABANDON,
    /** Tableau 5.2.4 - Inactives historique (avant introduction de ARRETEE/ABANDON). */
    INACTIVE
}
