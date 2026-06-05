package com.example.pfsagefemme.modules.adminposte.domain.port.in;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/** Ecran "Sages-femmes / Effectif" du chef de poste. */
public interface GetEquipePosteUseCase {

    record SageFemmeWithStats(
            UUID id,
            String matricule,
            String prenom,
            String nom,
            String username,
            boolean actif,
            int nbPatientes,
            LocalDate derniereActivite
    ) {}

    record EquipePoste(
            String posteNom,
            int totalSagesFemmes,
            List<SageFemmeWithStats> sagesFemmes,
            double moyennePatientesParSf,
            LocalDate dernierRapportDate,
            boolean conformite
    ) {}

    EquipePoste load(UUID posteSanteId);
}
