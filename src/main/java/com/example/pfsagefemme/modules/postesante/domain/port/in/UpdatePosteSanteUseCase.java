package com.example.pfsagefemme.modules.postesante.domain.port.in;

import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
import com.example.pfsagefemme.modules.postesante.domain.port.in.command.UpdatePosteSanteCommand;

public interface UpdatePosteSanteUseCase {
    PosteSante update(UpdatePosteSanteCommand command);
}
