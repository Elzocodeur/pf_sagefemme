package com.example.pfsagefemme.shared.domain.enums;

/**
 * Type d'une visite affichee dans le "Journal du jour".
 *
 *  - NOUVELLE_PV : 1ere visite jamais (= ouverture du dossier ce jour)
 *  - ANCIENNE_PV : patiente inscrite avant le mois courant, vue ce mois
 *  - SUIVI       : visite de suivi habituelle
 */
public enum TypeVisiteEnum {
    NOUVELLE_PV,
    ANCIENNE_PV,
    SUIVI
}
