package com.example.pfsagefemme.modules.contraception.infrastructure.persistence;

import com.example.pfsagefemme.modules.contraception.domain.model.entity.ChangementMethode;
import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChangementMethodeJpaRepository extends JpaRepository<ChangementMethode, UUID> {

    List<ChangementMethode> findByPatienteId(UUID patienteId);

    List<ChangementMethode> findByDateBetween(LocalDate from, LocalDate to);

    @Query("""
            SELECT COUNT(c)
            FROM ChangementMethode c
            WHERE c.ancienneMethode = :source
              AND c.nouvelleMethode = :cible
              AND c.date BETWEEN :from AND :to
              AND (:posteSanteId IS NULL OR c.patiente.posteSante.id = :posteSanteId)
            """)
    long countByMethodeSourceAndMethodeCibleAndDateBetween(
            @Param("source") MethodeContraceptiveEnum source,
            @Param("cible") MethodeContraceptiveEnum cible,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("posteSanteId") UUID posteSanteId
    );

    Optional<ChangementMethode> findFirstByPatienteIdAndDate(UUID patienteId, LocalDate date);

    @Query("""
            SELECT c FROM ChangementMethode c
            WHERE c.date BETWEEN :from AND :to
              AND (:posteSanteId IS NULL OR c.patiente.posteSante.id = :posteSanteId)
            ORDER BY c.date
            """)
    List<ChangementMethode> findByDateBetweenAndPosteSanteId(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("posteSanteId") UUID posteSanteId
    );
}
