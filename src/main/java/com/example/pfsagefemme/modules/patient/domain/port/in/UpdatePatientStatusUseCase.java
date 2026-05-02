package com.example.pfsagefemme.modules.patient.domain.port.in;

import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;

import java.time.LocalDate;
import java.util.UUID;

/** Transitions explicites du cycle de vie d'une patiente. */
public interface UpdatePatientStatusUseCase {

    record ArretCommand(UUID patienteId, String motif, LocalDate dateArret) {}
    record RepriseCommand(UUID patienteId, String observations) {}

    Patiente arreter(ArretCommand command);

    Patiente reprendre(RepriseCommand command);
}
