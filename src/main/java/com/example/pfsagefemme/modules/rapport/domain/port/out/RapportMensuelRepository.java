package com.example.pfsagefemme.modules.rapport.domain.port.out;

import com.example.pfsagefemme.modules.rapport.domain.model.entity.RapportMensuel;
import com.example.pfsagefemme.shared.domain.enums.StatutRapportEnum;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RapportMensuelRepository {

    RapportMensuel save(RapportMensuel rapport);

    Optional<RapportMensuel> findById(UUID id);

    Optional<RapportMensuel> findByMoisAndAnneeAndSageFemmeId(int mois, int annee, UUID sageFemmeId);

    List<RapportMensuel> findByPosteSanteId(UUID posteSanteId);

    List<RapportMensuel> findBySageFemmeId(UUID sageFemmeId);

    List<RapportMensuel> findByMoisAndAnnee(int mois, int annee);

    List<RapportMensuel> findByStatut(StatutRapportEnum statut);

    void deleteById(UUID id);
}
