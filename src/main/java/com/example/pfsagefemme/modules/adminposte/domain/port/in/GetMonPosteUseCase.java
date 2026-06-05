package com.example.pfsagefemme.modules.adminposte.domain.port.in;

import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;

import java.util.UUID;

/** Ecran "Mon poste de sante / Configuration de l'unite" - lecture. */
public interface GetMonPosteUseCase {
    PosteSante load(UUID posteSanteId);
}
