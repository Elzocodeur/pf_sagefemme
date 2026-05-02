package com.example.pfsagefemme.modules.consultation.domain.port.in;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.consultation.domain.port.in.command.CreateVisiteCommand;

public interface CreateVisiteUseCase {
    Visite create(CreateVisiteCommand command);
}
