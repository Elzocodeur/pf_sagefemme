package com.example.pfsagefemme.modules.postesante.domain.port.in;

import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;

import java.util.UUID;

/** Activation / desactivation d'un poste de sante. */
public interface TogglePosteSanteUseCase {
    PosteSante activate(UUID id);
    PosteSante deactivate(UUID id);
}
