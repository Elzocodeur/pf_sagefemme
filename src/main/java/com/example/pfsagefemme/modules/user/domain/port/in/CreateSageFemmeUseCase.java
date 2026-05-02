package com.example.pfsagefemme.modules.user.domain.port.in;

import com.example.pfsagefemme.modules.user.domain.model.entity.SageFemme;
import com.example.pfsagefemme.modules.user.domain.port.in.command.CreateSageFemmeCommand;

import java.util.UUID;

public interface CreateSageFemmeUseCase {
    /**
     * @param adminPosteId id de l'admin qui cree (utilise pour resoudre le poste de rattachement)
     */
    SageFemme create(UUID adminPosteId, CreateSageFemmeCommand command);
}
