package com.example.pfsagefemme.modules.patient.domain.port.out;

import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port sortant - operations de persistance et de comptage des patientes.
 * Les methodes "countBy..." servent a alimenter les tableaux du rapport mensuel.
 */
public interface PatienteRepository {

    Patiente save(Patiente patiente);

    Optional<Patiente> findById(UUID id);

    List<Patiente> findAll();

    List<Patiente> findByPosteSanteId(UUID posteSanteId);

    List<Patiente> findBySageFemmeId(UUID sageFemmeId);

    List<Patiente> findByStatutPatiente(StatutPatienteEnum statut);

    List<Patiente> findByPvvihTrue();

    /** Recherche plein-texte simple sur nom + prenom (insensible a la casse). */
    List<Patiente> search(String query, UUID posteSanteId);

    // ====== PVVIH (Section 5.2.3) ============================================

    long countByPvvihTrue(UUID posteSanteId);

    long countByPvvihTrueAndStatutPatiente(StatutPatienteEnum statut, UUID posteSanteId);

    long countByPvvihTrueAndDateInscriptionBetween(LocalDate from, LocalDate to, UUID posteSanteId);

    List<Patiente> findPvvih(UUID posteSanteId);

    // ====== Cycle de vie =====================================================

    List<Patiente> findByStatutPatienteIn(List<StatutPatienteEnum> statuts, UUID posteSanteId);

    /**
     * Patientes ACTIVE dont le dernier RDV planifie est depasse depuis plus de N jours
     * (= candidates "abandon").
     */
    List<Patiente> findInactivesSinceThreshold(LocalDate thresholdDate, UUID posteSanteId);

    long countByYearOfDateInscription(int year);

    Optional<Patiente> findByNumeroDossier(String numeroDossier);

    /**
     * Comptage pour les tableaux 5.2.1 / 5.2.2 / 5.2.4 du rapport mensuel.
     * Une "tranche d'age" est un intervalle [ageMin, ageMax] inclusif.
     */
    long countByStatutPatienteAndMethodeAndTrancheAge(
            StatutPatienteEnum statut,
            MethodeContraceptiveEnum methode,
            int ageMin,
            int ageMax,
            UUID posteSanteId
    );

    // ====== Comptages avec fenetre temporelle (rapport mensuel) ==============

    long countNouvellesByMethodeAndAge(MethodeContraceptiveEnum methode,
                                       int ageMin, int ageMax,
                                       LocalDate from, LocalDate to,
                                       UUID posteSanteId);

    long countActivesByMethodeAndAge(MethodeContraceptiveEnum methode,
                                     int ageMin, int ageMax,
                                     UUID posteSanteId);

    long countInactivesByMethodeAndAge(MethodeContraceptiveEnum methode,
                                       int ageMin, int ageMax,
                                       LocalDate from, LocalDate to,
                                       UUID posteSanteId);

    long countPvvihActivesByAge(int ageMin, int ageMax, UUID posteSanteId);

    // ====== Compteurs pour le journal du jour ================================

    long countNouvellesOnDate(LocalDate date, UUID posteSanteId);

    long countArreteesOnDate(LocalDate date, UUID posteSanteId);

    // ====== Compteurs Dashboard sage-femme ===================================

    long countAll(UUID posteSanteId);

    long countByStatut(StatutPatienteEnum statut, UUID posteSanteId);

    long countPvvih(UUID posteSanteId);

    long countNouvellesInscritesEntre(LocalDate from, LocalDate to, UUID posteSanteId);

    long countArreteesEntre(LocalDate from, LocalDate to, UUID posteSanteId);

    /** Patientes ACTIVE triees par date de derniere visite descendante - pour la card "Actives recentes". */
    List<Patiente> findActivesOrderByLastVisitDesc(UUID posteSanteId, int limit);

    void deleteById(UUID id);
}
