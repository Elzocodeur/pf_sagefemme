package com.example.pfsagefemme.modules.postesante.domain.port.in;

import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;

import java.util.UUID;

public interface GetPosteSanteUseCase {
    PosteSante byId(UUID id);
}
