package com.example.pfsagefemme.modules.patient.domain.port.out;

import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port sortant - operations de persistance et de comptage des patientes.
 * Les methodes "countBy..." servent a alimenter les tableaux du rapport mensuel.
 */
public interface PatienteRepository {

    /** Nombre de jours sans visite au-dela duquel une patiente est consideree en abandon. */
    int ABANDON_THRESHOLD_DAYS = 90;


    Patiente save(Patiente patiente);

    Optional<Patiente> findById(UUID id);

    List<Patiente> findAll();

    List<Patiente> findByPosteSanteId(UUID posteSanteId);

    List<Patiente> findBySageFemmeId(UUID sageFemmeId);

    List<Patiente> findByStatutPatiente(StatutPatienteEnum statut);

    List<Patiente> findByPvvihTrue();

    /** Recherche plein-texte simple sur nom + prenom (insensible a la casse). */
    List<Patiente> search(String query, UUID posteSanteId);

    /** Version paginee de la recherche. */
    Page<Patiente> searchPaged(String query, UUID posteSanteId, Pageable pageable);

    Page<Patiente> findByStatutPatienteInPaged(
        List<StatutPatienteEnum> statuts, UUID posteSanteId, String q, Pageable pageable);

    Page<Patiente> findInactivesSinceThresholdPaged(
        LocalDate thresholdDate, UUID posteSanteId, String q, Pageable pageable);

    Page<Patiente> findPvvihPaged(UUID posteSanteId, String q, Pageable pageable);

    // ====== PVVIH (Section 5.2.3) ============================================

    long countByPvvihTrue(UUID posteSanteId);

    long countByPvvihTrueAndStatutPatiente(StatutPatienteEnum statut, UUID posteSanteId);

    long countByPvvihTrueAndDateInscriptionBetween(LocalDate from, LocalDate to, UUID posteSanteId);

    List<Patiente> findPvvih(UUID posteSanteId);

    // ====== Cycle de vie =====================================================

    List<Patiente> findByStatutPatienteIn(List<StatutPatienteEnum> statuts, UUID posteSanteId);

    /**
     * Abandons : statut ABANDON, ou patiente ACTIVE/NOUVELLE inscrite avant le seuil
     * et sans visite depuis le seuil. Definition unique utilisee par tous les ecrans.
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
                                       UUID posteSanteId, UUID sageFemmeId);

    default long countNouvellesByMethodeAndAge(MethodeContraceptiveEnum methode, int ageMin, int ageMax,
                                               LocalDate from, LocalDate to, UUID posteSanteId) {
        return countNouvellesByMethodeAndAge(methode, ageMin, ageMax, from, to, posteSanteId, null);
    }

    long countActivesByMethodeAndAge(MethodeContraceptiveEnum methode,
                                     int ageMin, int ageMax,
                                     UUID posteSanteId, UUID sageFemmeId);

    default long countActivesByMethodeAndAge(MethodeContraceptiveEnum methode, int ageMin, int ageMax, UUID posteSanteId) {
        return countActivesByMethodeAndAge(methode, ageMin, ageMax, posteSanteId, null);
    }

    /** Inactives du mois : arretees dans le mois, inactives, ou en abandon (definition unique). */
    long countInactivesByMethodeAndAge(MethodeContraceptiveEnum methode,
                                       int ageMin, int ageMax,
                                       LocalDate from, LocalDate to,
                                       UUID posteSanteId, UUID sageFemmeId);

    default long countInactivesByMethodeAndAge(MethodeContraceptiveEnum methode, int ageMin, int ageMax,
                                               LocalDate from, LocalDate to, UUID posteSanteId) {
        return countInactivesByMethodeAndAge(methode, ageMin, ageMax, from, to, posteSanteId, null);
    }

    long countPvvihActivesByAge(int ageMin, int ageMax, UUID posteSanteId, UUID sageFemmeId);

    default long countPvvihActivesByAge(int ageMin, int ageMax, UUID posteSanteId) {
        return countPvvihActivesByAge(ageMin, ageMax, posteSanteId, null);
    }

    /** Nombre d'abandons a ce jour (definition unique, cf. {@link #ABANDON_THRESHOLD_DAYS}). */
    long countAbandons(UUID posteSanteId, UUID sageFemmeId);

    /** Anciennes PV du mois : inscrites avant {@code from} et revues entre {@code from} et {@code to}. */
    long countAnciennesPv(LocalDate from, LocalDate to, UUID posteSanteId, UUID sageFemmeId);

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
