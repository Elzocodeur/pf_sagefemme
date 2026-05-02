package com.example.pfsagefemme.modules.consultation.domain.port.out;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.FicheConsultation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FicheConsultationRepository {

    FicheConsultation save(FicheConsultation fiche);

    Optional<FicheConsultation> findById(UUID id);

    Optional<FicheConsultation> findByNumero(String numero);

    Optional<FicheConsultation> findByPatienteId(UUID patienteId);

    List<FicheConsultation> findBySageFemmeId(UUID sageFemmeId);

    List<FicheConsultation> findByPosteSanteId(UUID posteSanteId);

    List<FicheConsultation> findByDateConsultationBetween(LocalDate from, LocalDate to);

    void deleteById(UUID id);
}
