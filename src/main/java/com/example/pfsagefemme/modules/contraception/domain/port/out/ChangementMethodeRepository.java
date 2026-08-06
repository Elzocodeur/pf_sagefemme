package com.example.pfsagefemme.modules.contraception.domain.port.out;

import com.example.pfsagefemme.modules.contraception.domain.model.entity.ChangementMethode;
import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChangementMethodeRepository {

    ChangementMethode save(ChangementMethode changement);

    Optional<ChangementMethode> findById(UUID id);

    List<ChangementMethode> findByPatienteId(UUID patienteId);

    List<ChangementMethode> findByDateBetween(LocalDate from, LocalDate to);

    /** Liste des changements d'un mois, restreints a un poste si non null - alimente la matrice 5.2.1. */
    List<ChangementMethode> findByDateBetweenAndPosteSanteId(LocalDate from, LocalDate to, UUID posteSanteId);

    /** Comptage pour la matrice de changement du rapport mensuel (5.2.1). */
    long countByMethodeSourceAndMethodeCibleAndDateBetween(
            MethodeContraceptiveEnum source,
            MethodeContraceptiveEnum cible,
            LocalDate from,
            LocalDate to,
            UUID posteSanteId
    );

    void deleteById(UUID id);

    Optional<ChangementMethode> findByPatienteIdAndDate(UUID patienteId, LocalDate date);
}
