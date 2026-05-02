package com.example.pfsagefemme.modules.consultation.infrastructure.persistence;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VisiteJpaRepository extends JpaRepository<Visite, UUID> {

    List<Visite> findByPatienteIdOrderByDateDesc(UUID patienteId);

    Optional<Visite> findFirstByPatienteIdOrderByDateDesc(UUID patienteId);

    List<Visite> findByFicheConsultationId(UUID ficheId);

    List<Visite> findByDateBetween(LocalDate from, LocalDate to);

    List<Visite> findBySageFemmeIdAndDateBetween(UUID sageFemmeId, LocalDate from, LocalDate to);

    @Query("""
            SELECT v FROM Visite v
            WHERE v.date = :date
              AND (:posteSanteId IS NULL OR v.patiente.posteSante.id = :posteSanteId)
            ORDER BY v.createdAt DESC
            """)
    List<Visite> findByDateAndPosteSanteId(@Param("date") LocalDate date,
                                           @Param("posteSanteId") UUID posteSanteId);

    long countByPatienteIdAndDateLessThanEqual(UUID patienteId, LocalDate date);

    long countByPatienteIdAndDateGreaterThanEqual(UUID patienteId, LocalDate since);

    @Query("""
            SELECT COUNT(v) FROM Visite v
            WHERE v.date = :date
              AND (:posteSanteId IS NULL OR v.patiente.posteSante.id = :posteSanteId)
            """)
    long countByDateAndPosteSanteId(@Param("date") LocalDate date,
                                    @Param("posteSanteId") UUID posteSanteId);
}
