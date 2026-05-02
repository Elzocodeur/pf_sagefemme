package com.example.pfsagefemme.modules.postesante.domain.port.in;

import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
import com.example.pfsagefemme.modules.postesante.domain.port.in.command.CreatePosteSanteCommand;

public interface CreatePosteSanteUseCase {
    PosteSante create(CreatePosteSanteCommand command);
}
