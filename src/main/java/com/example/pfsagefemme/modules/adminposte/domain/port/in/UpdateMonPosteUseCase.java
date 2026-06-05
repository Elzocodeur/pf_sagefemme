package com.example.pfsagefemme.modules.adminposte.domain.port.in;

import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;

import java.util.UUID;

/** Ecran "Mon poste de sante / Configuration de l'unite" - modification. */
public interface UpdateMonPosteUseCase {

    record Command(
            String nom,
            String district,
            String region,
            String responsableNom,
            String telephone,
            String responsableEmail,
            String responsableTelephone,
            String responsableTitre
    ) {}

    PosteSante update(UUID posteSanteId, Command command);
}
