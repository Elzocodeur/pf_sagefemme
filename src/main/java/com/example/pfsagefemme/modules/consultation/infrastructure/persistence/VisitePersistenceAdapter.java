package com.example.pfsagefemme.modules.consultation.infrastructure.persistence;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.consultation.domain.port.out.VisiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VisitePersistenceAdapter implements VisiteRepository {

    private final VisiteJpaRepository jpaRepository;

    @Override
    public Visite save(Visite visite) {
        return jpaRepository.save(visite);
    }

    @Override
    public Optional<Visite> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Visite> findByPatienteId(UUID patienteId) {
        return jpaRepository.findByPatienteIdOrderByDateDesc(patienteId);
    }

    @Override
    public Optional<Visite> findLatestByPatienteId(UUID patienteId) {
        return jpaRepository.findFirstByPatienteIdOrderByDateDesc(patienteId);
    }

    @Override
    public List<Visite> findByFicheConsultationId(UUID ficheId) {
        return jpaRepository.findByFicheConsultationId(ficheId);
    }

    @Override
    public List<Visite> findByDateBetween(LocalDate from, LocalDate to) {
        return jpaRepository.findByDateBetween(from, to);
    }

    @Override
    public List<Visite> findBySageFemmeIdAndDateBetween(UUID sageFemmeId, LocalDate from, LocalDate to) {
        return jpaRepository.findBySageFemmeIdAndDateBetween(sageFemmeId, from, to);
    }

    @Override
    public List<Visite> findByDateAndPosteSanteId(LocalDate date, UUID posteSanteId) {
        return jpaRepository.findByDateAndPosteSanteId(date, posteSanteId);
    }

    @Override
    public long countByDateAndPosteSanteId(LocalDate date, UUID posteSanteId) {
        return jpaRepository.countByDateAndPosteSanteId(date, posteSanteId);
    }

    @Override
    public long countByPatienteIdAndDateGreaterThanEqual(UUID patienteId, LocalDate since) {
        return jpaRepository.countByPatienteIdAndDateGreaterThanEqual(patienteId, since);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
