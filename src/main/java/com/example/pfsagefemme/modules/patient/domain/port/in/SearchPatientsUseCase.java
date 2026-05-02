package com.example.pfsagefemme.modules.patient.domain.port.in;

import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;

import java.util.List;
import java.util.UUID;

public interface SearchPatientsUseCase {
    /**
     * @param query     terme de recherche (nom ou prenom partiel, peut etre null = tout)
     * @param posteSanteId si non null, restreint au poste donne
     */
    List<Patiente> search(String query, UUID posteSanteId);
}
