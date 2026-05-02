package com.example.pfsagefemme.modules.user.domain.port.out;

import com.example.pfsagefemme.modules.user.domain.model.entity.SageFemme;
import com.example.pfsagefemme.modules.user.domain.model.entity.Utilisateur;
import com.example.pfsagefemme.shared.domain.enums.RoleEnum;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Port sortant - persistance des utilisateurs (toutes sous-classes). */
public interface UtilisateurRepository {

    <T extends Utilisateur> T save(T utilisateur);

    Optional<Utilisateur> findById(UUID id);

    Optional<Utilisateur> findByEmail(String email);

    Optional<Utilisateur> findByUsername(String username);

    /** Trouve par email OU par username (pour les login flexibles). */
    Optional<Utilisateur> findByEmailOrUsername(String identifier);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<Utilisateur> findAll();

    List<Utilisateur> findByRole(RoleEnum role);

    List<Utilisateur> findByActifTrue();

    Optional<SageFemme> findSageFemmeByMatricule(String matricule);

    List<SageFemme> findSageFemmesByPosteSanteId(UUID posteSanteId);

    void deleteById(UUID id);
}
