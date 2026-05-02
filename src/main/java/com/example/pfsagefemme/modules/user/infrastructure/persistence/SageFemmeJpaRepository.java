package com.example.pfsagefemme.modules.user.infrastructure.persistence;

import com.example.pfsagefemme.modules.user.domain.model.entity.SageFemme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SageFemmeJpaRepository extends JpaRepository<SageFemme, UUID> {

    Optional<SageFemme> findByMatricule(String matricule);

    List<SageFemme> findByPosteSanteId(UUID posteSanteId);
}
