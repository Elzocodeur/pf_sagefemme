package com.example.pfsagefemme.modules.patient.domain.port.in;

import java.util.UUID;

public interface GetPatientStatusDashboardUseCase {

    /** Compteurs de l'ecran "Statuts patientes". */
    record StatusStats(
            long arretees,
            long arreteesCeMois,
            long abandons,
            long anciennesReprises
    ) {}

    StatusStats stats(UUID posteSanteId);
}
