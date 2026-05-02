package com.example.pfsagefemme.modules.postesante.domain.port.in.command;

import java.util.UUID;

/** Toutes les valeurs sont optionnelles : les nulls = on ne touche pas au champ. */
public record UpdatePosteSanteCommand(
        UUID id,
        String nom,
        String adresse,
        String telephone,
        String district,
        String region,
        String responsableNom,
        String responsableTitre,
        String responsableEmail,
        String responsableTelephone
) {}
