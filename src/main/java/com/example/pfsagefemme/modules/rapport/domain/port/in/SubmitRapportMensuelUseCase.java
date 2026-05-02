package com.example.pfsagefemme.modules.rapport.domain.port.in;

import com.example.pfsagefemme.modules.rapport.domain.model.entity.RapportMensuel;

import java.util.UUID;

/** Transitions de cycle de vie : VALIDE / SOUMIS. */
public interface SubmitRapportMensuelUseCase {
    RapportMensuel valider(UUID rapportId);
    RapportMensuel soumettre(UUID rapportId);
}
