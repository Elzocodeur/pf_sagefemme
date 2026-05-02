package com.example.pfsagefemme.modules.patient.domain.port.in;

import java.util.UUID;

public interface GetPvvihDashboardUseCase {

    /** Compteurs pour les 3 cards "Femmes PVVIH" - Section 5.2.3. */
    record PvvihStats(
            long totalAnnuel,
            long actives,
            long nouvellesCeMois
    ) {}

    PvvihStats stats(UUID posteSanteId);
}
