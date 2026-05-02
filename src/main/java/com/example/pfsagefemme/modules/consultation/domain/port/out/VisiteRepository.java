package com.example.pfsagefemme.modules.consultation.domain.port.out;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VisiteRepository {

    Visite save(Visite visite);

    Optional<Visite> findById(UUID id);

    List<Visite> findByPatienteId(UUID patienteId);

    /** Visite la plus recente d'une patiente (par date de visite descendante). */
    Optional<Visite> findLatestByPatienteId(UUID patienteId);

    List<Visite> findByFicheConsultationId(UUID ficheId);

    List<Visite> findByDateBetween(LocalDate from, LocalDate to);

    List<Visite> findBySageFemmeIdAndDateBetween(UUID sageFemmeId, LocalDate from, LocalDate to);

    /** Visites d'un jour donne, restreintes a un poste si non null. */
    List<Visite> findByDateAndPosteSanteId(LocalDate date, UUID posteSanteId);

    /** Comptage rapide pour le dashboard - une journee. */
    long countByDateAndPosteSanteId(LocalDate date, UUID posteSanteId);

    /** Compte les visites recentes d'une patiente (>= since) - alimente le badge clinique. */
    long countByPatienteIdAndDateGreaterThanEqual(UUID patienteId, LocalDate since);

    void deleteById(UUID id);
}
