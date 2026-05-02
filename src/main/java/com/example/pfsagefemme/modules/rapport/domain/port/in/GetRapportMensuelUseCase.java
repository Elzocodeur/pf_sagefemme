package com.example.pfsagefemme.modules.rapport.domain.port.in;

import com.example.pfsagefemme.modules.rapport.domain.model.RapportData;

import java.util.Optional;
import java.util.UUID;

public interface GetRapportMensuelUseCase {
    /** Lecture par id d'un rapport persiste. */
    RapportData byId(UUID id);

    /** Lecture par periode + sage-femme - facilite l'idempotence cote frontend. */
    Optional<RapportData> byPeriode(int mois, int annee, UUID sageFemmeId);
}
