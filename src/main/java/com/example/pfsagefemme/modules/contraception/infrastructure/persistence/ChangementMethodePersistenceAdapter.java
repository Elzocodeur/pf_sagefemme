package com.example.pfsagefemme.modules.contraception.infrastructure.persistence;

import com.example.pfsagefemme.modules.contraception.domain.model.entity.ChangementMethode;
import com.example.pfsagefemme.modules.contraception.domain.port.out.ChangementMethodeRepository;
import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChangementMethodePersistenceAdapter implements ChangementMethodeRepository {

    private final ChangementMethodeJpaRepository jpaRepository;

    @Override
    public ChangementMethode save(ChangementMethode changement) {
        return jpaRepository.save(changement);
    }

    @Override
    public Optional<ChangementMethode> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<ChangementMethode> findByPatienteId(UUID patienteId) {
        return jpaRepository.findByPatienteId(patienteId);
    }

    @Override
    public List<ChangementMethode> findByDateBetween(LocalDate from, LocalDate to) {
        return jpaRepository.findByDateBetween(from, to);
    }

    @Override
    public List<ChangementMethode> findByDateBetweenAndPosteSanteId(LocalDate from, LocalDate to, UUID posteSanteId) {
        return jpaRepository.findByDateBetweenAndPosteSanteId(from, to, posteSanteId);
    }

    @Override
    public long countByMethodeSourceAndMethodeCibleAndDateBetween(
            MethodeContraceptiveEnum source,
            MethodeContraceptiveEnum cible,
            LocalDate from,
            LocalDate to,
            UUID posteSanteId
    ) {
        return jpaRepository.countByMethodeSourceAndMethodeCibleAndDateBetween(source, cible, from, to, posteSanteId);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Optional<ChangementMethode> findByPatienteIdAndDate(UUID patienteId, LocalDate date) {
        return jpaRepository.findFirstByPatienteIdAndDate(patienteId, date);
    }
}
