package com.example.pfsagefemme.shared.domain.enums;

/**
 * Etat clinique calcule d'une patiente, affiche en badge dans les listings.
 *  - STABLE              : suivi standard
 *  - SUIVI_HEBDO         : suivi rapproche (PVVIH ou multiples visites recentes)
 *  - SOUS_SURVEILLANCE   : suivi etroit (a etendre selon les criteres metier)
 */
public enum StatutCliniqueEnum {
    STABLE,
    SUIVI_HEBDO,
    SOUS_SURVEILLANCE
}
