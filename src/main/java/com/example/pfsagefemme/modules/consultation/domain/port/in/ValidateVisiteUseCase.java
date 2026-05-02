package com.example.pfsagefemme.modules.consultation.domain.port.in;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;

import java.util.UUID;

/** Passe une visite de BROUILLON a VALIDEE. */
public interface ValidateVisiteUseCase {
    Visite validate(UUID visiteId);
}
