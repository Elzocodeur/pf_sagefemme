package com.example.pfsagefemme.modules.rapport.infrastructure.persistence;

import com.example.pfsagefemme.modules.rapport.domain.model.entity.RapportMensuel;
import com.example.pfsagefemme.modules.rapport.domain.port.out.RapportMensuelRepository;
import com.example.pfsagefemme.shared.domain.enums.StatutRapportEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RapportMensuelPersistenceAdapter implements RapportMensuelRepository {

    private final RapportMensuelJpaRepository jpaRepository;

    @Override
    public RapportMensuel save(RapportMensuel rapport) {
        return jpaRepository.save(rapport);
    }

    @Override
    public Optional<RapportMensuel> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<RapportMensuel> findByMoisAndAnneeAndSageFemmeId(int mois, int annee, UUID sageFemmeId) {
        return jpaRepository.findByMoisAndAnneeAndSageFemmeId(mois, annee, sageFemmeId);
    }

    @Override
    public List<RapportMensuel> findByPosteSanteId(UUID posteSanteId) {
        return jpaRepository.findByPosteSanteId(posteSanteId);
    }

    @Override
    public List<RapportMensuel> findBySageFemmeId(UUID sageFemmeId) {
        return jpaRepository.findBySageFemmeId(sageFemmeId);
    }

    @Override
    public List<RapportMensuel> findByMoisAndAnnee(int mois, int annee) {
        return jpaRepository.findByMoisAndAnnee(mois, annee);
    }

    @Override
    public List<RapportMensuel> findByStatut(StatutRapportEnum statut) {
        return jpaRepository.findByStatut(statut);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
