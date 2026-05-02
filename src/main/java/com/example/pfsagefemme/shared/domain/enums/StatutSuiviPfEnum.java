package com.example.pfsagefemme.shared.domain.enums;

/**
 * Etat operationnel du suivi PF d'une patiente, calcule a la lecture
 * (PAS persiste). Sert aux dashboards (badges A_JOUR / RENOUVELLEMENT / URGENT).
 */
public enum StatutSuiviPfEnum {
    /** Prochain RDV au-dela de la fenetre de renouvellement. */
    A_JOUR,
    /** Prochain RDV imminent (sous 7 jours). */
    RENOUVELLEMENT,
    /** Prochain RDV depasse, ou aucune methode active. */
    URGENT
}
