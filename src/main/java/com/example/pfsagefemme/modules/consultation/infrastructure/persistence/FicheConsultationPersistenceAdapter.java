package com.example.pfsagefemme.modules.consultation.infrastructure.persistence;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.FicheConsultation;
import com.example.pfsagefemme.modules.consultation.domain.port.out.FicheConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FicheConsultationPersistenceAdapter implements FicheConsultationRepository {

    private final FicheConsultationJpaRepository jpaRepository;

    @Override
    public FicheConsultation save(FicheConsultation fiche) {
        return jpaRepository.save(fiche);
    }

    @Override
    public Optional<FicheConsultation> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<FicheConsultation> findByNumero(String numero) {
        return jpaRepository.findByNumero(numero);
    }

    @Override
    public Optional<FicheConsultation> findByPatienteId(UUID patienteId) {
        return jpaRepository.findByPatienteId(patienteId);
    }

    @Override
    public List<FicheConsultation> findBySageFemmeId(UUID sageFemmeId) {
        return jpaRepository.findBySageFemmeId(sageFemmeId);
    }

    @Override
    public List<FicheConsultation> findByPosteSanteId(UUID posteSanteId) {
        return jpaRepository.findByPosteSanteId(posteSanteId);
    }

    @Override
    public List<FicheConsultation> findByDateConsultationBetween(LocalDate from, LocalDate to) {
        return jpaRepository.findByDateConsultationBetween(from, to);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
