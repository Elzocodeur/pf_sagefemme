package com.example.pfsagefemme.modules.patient.infrastructure.persistence;

import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatienteJpaRepository extends JpaRepository<Patiente, UUID> {

    List<Patiente> findByPosteSanteId(UUID posteSanteId);

    List<Patiente> findBySageFemmeId(UUID sageFemmeId);

    List<Patiente> findByStatutPatiente(StatutPatienteEnum statut);

    List<Patiente> findByPvvihTrue();

    @Query("""
            SELECT p FROM Patiente p
            WHERE (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
              AND (LOWER(p.nom) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(p.prenom) LIKE LOWER(CONCAT('%', :q, '%')))
            ORDER BY p.nom, p.prenom
            """)
    List<Patiente> search(@Param("q") String query, @Param("posteSanteId") UUID posteSanteId);

    /**
     * Compte les patientes correspondant a un statut + une methode contraceptive
     * (lue sur la fiche de consultation associee) + une tranche d'age + un poste.
     */
    @Query("""
            SELECT COUNT(p)
            FROM Patiente p, FicheConsultation f
            WHERE f.patiente = p
              AND p.statutPatiente = :statut
              AND f.methodeChoisie = :methode
              AND p.age BETWEEN :ageMin AND :ageMax
              AND (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
            """)
    long countByStatutPatienteAndMethodeAndTrancheAge(
            @Param("statut") StatutPatienteEnum statut,
            @Param("methode") MethodeContraceptiveEnum methode,
            @Param("ageMin") int ageMin,
            @Param("ageMax") int ageMax,
            @Param("posteSanteId") UUID posteSanteId
    );

    // ====== PVVIH ============================================================

    @Query("""
            SELECT COUNT(p) FROM Patiente p
            WHERE p.pvvih = TRUE
              AND (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
            """)
    long countAllPvvih(@Param("posteSanteId") UUID posteSanteId);

    @Query("""
            SELECT COUNT(p) FROM Patiente p
            WHERE p.pvvih = TRUE
              AND p.statutPatiente = :statut
              AND (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
            """)
    long countPvvihByStatut(@Param("statut") StatutPatienteEnum statut,
                            @Param("posteSanteId") UUID posteSanteId);

    @Query("""
            SELECT COUNT(p) FROM Patiente p
            WHERE p.pvvih = TRUE
              AND p.dateInscription BETWEEN :from AND :to
              AND (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
            """)
    long countPvvihInscritesEntre(@Param("from") LocalDate from,
                                  @Param("to") LocalDate to,
                                  @Param("posteSanteId") UUID posteSanteId);

    @Query("""
            SELECT p FROM Patiente p
            WHERE p.pvvih = TRUE
              AND (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
            ORDER BY p.dateInscription DESC, p.nom
            """)
    List<Patiente> findPvvih(@Param("posteSanteId") UUID posteSanteId);

    // ====== Cycle de vie =====================================================

    @Query("""
            SELECT p FROM Patiente p
            WHERE p.statutPatiente IN :statuts
              AND (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
            ORDER BY p.dateArret DESC NULLS LAST, p.nom
            """)
    List<Patiente> findByStatutPatienteIn(@Param("statuts") List<StatutPatienteEnum> statuts,
                                          @Param("posteSanteId") UUID posteSanteId);

    /**
     * Definition unique de l'abandon : statut ABANDON, ou patiente ACTIVE/NOUVELLE
     * inscrite avant le seuil et sans aucune visite depuis le seuil.
     */
    @Query("""
            SELECT p FROM Patiente p
            WHERE (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
              AND (p.statutPatiente = com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum.ABANDON
                   OR (p.statutPatiente IN (com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum.ACTIVE,
                                            com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum.NOUVELLE)
                       AND p.dateInscription < :threshold
                       AND NOT EXISTS (SELECT 1 FROM Visite v WHERE v.patiente = p AND v.date >= :threshold)))
            ORDER BY p.dateInscription
            """)
    List<Patiente> findInactivesSince(@Param("threshold") LocalDate threshold,
                                      @Param("posteSanteId") UUID posteSanteId);

    @Query("""
            SELECT COUNT(p) FROM Patiente p
            WHERE EXTRACT(YEAR FROM p.dateInscription) = :year
            """)
    long countByYearOfDateInscription(@Param("year") int year);

    Optional<Patiente> findByNumeroDossier(String numeroDossier);

    // ====== Comptages avec fenetre temporelle (rapport mensuel) ==============

    @Query("""
            SELECT COUNT(DISTINCT p)
            FROM Patiente p, FicheConsultation f
            WHERE f.patiente = p
              AND p.dateInscription BETWEEN :from AND :to
              AND f.methodeChoisie = :methode
              AND p.age BETWEEN :ageMin AND :ageMax
              AND (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
              AND (:sageFemmeId IS NULL OR p.sageFemme.id = :sageFemmeId)
            """)
    long countNouvellesByMethodeAndAge(@Param("methode") MethodeContraceptiveEnum methode,
                                       @Param("ageMin") int ageMin,
                                       @Param("ageMax") int ageMax,
                                       @Param("from") LocalDate from,
                                       @Param("to") LocalDate to,
                                       @Param("posteSanteId") UUID posteSanteId,
                                       @Param("sageFemmeId") UUID sageFemmeId);

    @Query("""
            SELECT COUNT(DISTINCT p)
            FROM Patiente p, FicheConsultation f
            WHERE f.patiente = p
              AND p.statutPatiente = com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum.ACTIVE
              AND f.methodeChoisie = :methode
              AND p.age BETWEEN :ageMin AND :ageMax
              AND (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
              AND (:sageFemmeId IS NULL OR p.sageFemme.id = :sageFemmeId)
            """)
    long countActivesByMethodeAndAge(@Param("methode") MethodeContraceptiveEnum methode,
                                     @Param("ageMin") int ageMin,
                                     @Param("ageMax") int ageMax,
                                     @Param("posteSanteId") UUID posteSanteId,
                                     @Param("sageFemmeId") UUID sageFemmeId);

    @Query("""
            SELECT COUNT(DISTINCT p)
            FROM Patiente p, FicheConsultation f
            WHERE f.patiente = p
              AND f.methodeChoisie = :methode
              AND p.age BETWEEN :ageMin AND :ageMax
              AND (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
              AND (:sageFemmeId IS NULL OR p.sageFemme.id = :sageFemmeId)
              AND (
                  (p.statutPatiente = com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum.ARRETEE
                       AND p.dateArret BETWEEN :from AND :to)
                  OR p.statutPatiente = com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum.INACTIVE
                  OR (p.statutPatiente = com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum.ABANDON
                     OR (p.statutPatiente IN (com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum.ACTIVE,
                                              com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum.NOUVELLE)
                         AND p.dateInscription < :threshold
                         AND NOT EXISTS (SELECT 1 FROM Visite v WHERE v.patiente = p AND v.date >= :threshold)))
              )
            """)
    long countInactivesByMethodeAndAge(@Param("methode") MethodeContraceptiveEnum methode,
                                       @Param("ageMin") int ageMin,
                                       @Param("ageMax") int ageMax,
                                       @Param("from") LocalDate from,
                                       @Param("to") LocalDate to,
                                       @Param("threshold") LocalDate threshold,
                                       @Param("posteSanteId") UUID posteSanteId,
                                       @Param("sageFemmeId") UUID sageFemmeId);

    @Query("""
            SELECT COUNT(p) FROM Patiente p
            WHERE p.pvvih = TRUE
              AND p.statutPatiente = com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum.ACTIVE
              AND p.age BETWEEN :ageMin AND :ageMax
              AND (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
              AND (:sageFemmeId IS NULL OR p.sageFemme.id = :sageFemmeId)
            """)
    long countPvvihActivesByAge(@Param("ageMin") int ageMin,
                                @Param("ageMax") int ageMax,
                                @Param("posteSanteId") UUID posteSanteId,
                                @Param("sageFemmeId") UUID sageFemmeId);

    /** Abandons (definition unique, cf. findInactivesSince). */
    @Query("""
            SELECT COUNT(p) FROM Patiente p
            WHERE (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
              AND (:sageFemmeId IS NULL OR p.sageFemme.id = :sageFemmeId)
              AND (p.statutPatiente = com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum.ABANDON
                   OR (p.statutPatiente IN (com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum.ACTIVE,
                                            com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum.NOUVELLE)
                       AND p.dateInscription < :threshold
                       AND NOT EXISTS (SELECT 1 FROM Visite v WHERE v.patiente = p AND v.date >= :threshold)))
            """)
    long countAbandons(@Param("threshold") LocalDate threshold,
                       @Param("posteSanteId") UUID posteSanteId,
                       @Param("sageFemmeId") UUID sageFemmeId);

    /** Anciennes PV : inscrites avant le mois et revues (visite validee) pendant le mois. */
    @Query("""
            SELECT COUNT(DISTINCT p) FROM Patiente p
            WHERE p.dateInscription < :from
              AND (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
              AND (:sageFemmeId IS NULL OR p.sageFemme.id = :sageFemmeId)
              AND EXISTS (SELECT 1 FROM Visite v
                          WHERE v.patiente = p AND v.date BETWEEN :from AND :to
                            AND v.statut = com.example.pfsagefemme.shared.domain.enums.StatutVisiteEnum.VALIDEE)
            """)
    long countAnciennesPv(@Param("from") LocalDate from,
                          @Param("to") LocalDate to,
                          @Param("posteSanteId") UUID posteSanteId,
                          @Param("sageFemmeId") UUID sageFemmeId);

    // ====== Journal du jour ==================================================

    @Query("""
            SELECT COUNT(p) FROM Patiente p
            WHERE p.dateInscription = :date
              AND (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
            """)
    long countNouvellesOnDate(@Param("date") LocalDate date,
                              @Param("posteSanteId") UUID posteSanteId);

    @Query("""
            SELECT COUNT(p) FROM Patiente p
            WHERE p.dateArret = :date
              AND (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
            """)
    long countArreteesOnDate(@Param("date") LocalDate date,
                             @Param("posteSanteId") UUID posteSanteId);

    // ====== Dashboard ========================================================

    @Query("""
            SELECT COUNT(p) FROM Patiente p
            WHERE (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
            """)
    long countAllByPoste(@Param("posteSanteId") UUID posteSanteId);

    @Query("""
            SELECT COUNT(p) FROM Patiente p
            WHERE p.statutPatiente = :statut
              AND (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
            """)
    long countByStatutAndPoste(@Param("statut") com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum statut,
                               @Param("posteSanteId") UUID posteSanteId);

    @Query("""
            SELECT COUNT(p) FROM Patiente p
            WHERE p.pvvih = TRUE
              AND (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
            """)
    long countPvvihByPoste(@Param("posteSanteId") UUID posteSanteId);

    @Query("""
            SELECT COUNT(p) FROM Patiente p
            WHERE p.dateInscription BETWEEN :from AND :to
              AND (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
            """)
    long countNouvellesInscritesEntre(@Param("from") LocalDate from,
                                      @Param("to") LocalDate to,
                                      @Param("posteSanteId") UUID posteSanteId);

    @Query("""
            SELECT COUNT(p) FROM Patiente p
            WHERE p.dateArret BETWEEN :from AND :to
              AND (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
            """)
    long countArreteesEntre(@Param("from") LocalDate from,
                            @Param("to") LocalDate to,
                            @Param("posteSanteId") UUID posteSanteId);

    /** Top N patientes ACTIVE triees par date de derniere visite descendante (NULLS LAST). */
    @Query("""
            SELECT p FROM Patiente p
            WHERE p.statutPatiente = com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum.ACTIVE
              AND (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
            ORDER BY (SELECT MAX(v.date) FROM Visite v WHERE v.patiente = p) DESC NULLS LAST,
                     p.dateInscription DESC
            """)
    List<Patiente> findActivesOrderByLastVisitDesc(@Param("posteSanteId") UUID posteSanteId,
                                                   Pageable pageable);

    // ====== Requetes paginées ================================================

    @Query("""
            SELECT p FROM Patiente p
            WHERE (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
              AND (:q IS NULL OR :q = ''
                   OR LOWER(p.nom) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(p.prenom) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(p.numeroDossier) LIKE LOWER(CONCAT('%', :q, '%')))
            ORDER BY p.nom, p.prenom
            """)
    Page<Patiente> searchPaged(@Param("q") String q,
                               @Param("posteSanteId") UUID posteSanteId,
                               Pageable pageable);

    @Query("""
            SELECT p FROM Patiente p
            WHERE p.statutPatiente IN :statuts
              AND (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
              AND (:q IS NULL OR :q = ''
                   OR LOWER(p.nom) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(p.prenom) LIKE LOWER(CONCAT('%', :q, '%')))
            ORDER BY p.dateArret DESC NULLS LAST, p.nom
            """)
    Page<Patiente> findByStatutPatienteInPaged(@Param("statuts") List<StatutPatienteEnum> statuts,
                                               @Param("posteSanteId") UUID posteSanteId,
                                               @Param("q") String q,
                                               Pageable pageable);

    @Query("""
            SELECT p FROM Patiente p
            WHERE (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
              AND (:q IS NULL OR :q = ''
                   OR LOWER(p.nom) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(p.prenom) LIKE LOWER(CONCAT('%', :q, '%')))
              AND (p.statutPatiente = com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum.ABANDON
                   OR (p.statutPatiente IN (com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum.ACTIVE,
                                            com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum.NOUVELLE)
                       AND p.dateInscription < :threshold
                       AND NOT EXISTS (SELECT 1 FROM Visite v WHERE v.patiente = p AND v.date >= :threshold)))
            ORDER BY p.dateInscription
            """)
    Page<Patiente> findInactivesSincePaged(@Param("threshold") LocalDate threshold,
                                           @Param("posteSanteId") UUID posteSanteId,
                                           @Param("q") String q,
                                           Pageable pageable);

    @Query("""
            SELECT p FROM Patiente p
            WHERE p.pvvih = TRUE
              AND (:posteSanteId IS NULL OR p.posteSante.id = :posteSanteId)
              AND (:q IS NULL OR :q = ''
                   OR LOWER(p.nom) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(p.prenom) LIKE LOWER(CONCAT('%', :q, '%')))
            ORDER BY p.dateInscription DESC, p.nom
            """)
    Page<Patiente> findPvvihPaged(@Param("posteSanteId") UUID posteSanteId,
                                   @Param("q") String q,
                                   Pageable pageable);
}
