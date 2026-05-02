package com.example.pfsagefemme.modules.consultation.infrastructure.persistence;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.FicheConsultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FicheConsultationJpaRepository extends JpaRepository<FicheConsultation, UUID> {

    Optional<FicheConsultation> findByNumero(String numero);

    Optional<FicheConsultation> findByPatienteId(UUID patienteId);

    List<FicheConsultation> findBySageFemmeId(UUID sageFemmeId);

    List<FicheConsultation> findByPosteSanteId(UUID posteSanteId);

    List<FicheConsultation> findByDateConsultationBetween(LocalDate from, LocalDate to);
}
