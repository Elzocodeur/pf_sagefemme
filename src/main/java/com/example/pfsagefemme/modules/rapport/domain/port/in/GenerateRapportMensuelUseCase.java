package com.example.pfsagefemme.modules.rapport.domain.port.in;

import com.example.pfsagefemme.modules.rapport.domain.model.RapportData;

import java.util.UUID;

public interface GenerateRapportMensuelUseCase {

    record Command(int mois, int annee, UUID posteSanteId, UUID sageFemmeId) {}

    /**
     * Genere (ou regenere si BROUILLON) le rapport pour la periode demandee.
     * Persiste l'entete et les lignes ; renvoie la vue agregee.
     */
    RapportData generate(Command command);
}
