package com.example.pfsagefemme.modules.rapport.infrastructure.persistence;

import com.example.pfsagefemme.modules.rapport.domain.model.entity.RapportMensuel;
import com.example.pfsagefemme.shared.domain.enums.StatutRapportEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RapportMensuelJpaRepository extends JpaRepository<RapportMensuel, UUID> {

    Optional<RapportMensuel> findByMoisAndAnneeAndSageFemmeId(Integer mois, Integer annee, UUID sageFemmeId);

    List<RapportMensuel> findByPosteSanteId(UUID posteSanteId);

    List<RapportMensuel> findBySageFemmeId(UUID sageFemmeId);

    List<RapportMensuel> findByMoisAndAnnee(Integer mois, Integer annee);

    List<RapportMensuel> findByStatut(StatutRapportEnum statut);
}
