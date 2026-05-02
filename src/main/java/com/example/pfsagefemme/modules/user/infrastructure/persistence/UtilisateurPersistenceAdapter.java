package com.example.pfsagefemme.modules.user.infrastructure.persistence;

import com.example.pfsagefemme.modules.user.domain.model.entity.SageFemme;
import com.example.pfsagefemme.modules.user.domain.model.entity.Utilisateur;
import com.example.pfsagefemme.modules.user.domain.port.out.UtilisateurRepository;
import com.example.pfsagefemme.shared.domain.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UtilisateurPersistenceAdapter implements UtilisateurRepository {

    private final UtilisateurJpaRepository utilisateurJpaRepository;
    private final SageFemmeJpaRepository sageFemmeJpaRepository;

    @Override
    public <T extends Utilisateur> T save(T utilisateur) {
        return utilisateurJpaRepository.save(utilisateur);
    }

    @Override
    public Optional<Utilisateur> findById(UUID id) {
        return utilisateurJpaRepository.findById(id);
    }

    @Override
    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurJpaRepository.findByEmail(email);
    }

    @Override
    public Optional<Utilisateur> findByUsername(String username) {
        return utilisateurJpaRepository.findByUsername(username);
    }

    @Override
    public Optional<Utilisateur> findByEmailOrUsername(String identifier) {
        return utilisateurJpaRepository.findByEmailOrUsername(identifier);
    }

    @Override
    public boolean existsByEmail(String email) {
        return utilisateurJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return utilisateurJpaRepository.existsByUsername(username);
    }

    @Override
    public List<Utilisateur> findAll() {
        return utilisateurJpaRepository.findAll();
    }

    @Override
    public List<Utilisateur> findByRole(RoleEnum role) {
        return utilisateurJpaRepository.findByRole(role);
    }

    @Override
    public List<Utilisateur> findByActifTrue() {
        return utilisateurJpaRepository.findByActifTrue();
    }

    @Override
    public Optional<SageFemme> findSageFemmeByMatricule(String matricule) {
        return sageFemmeJpaRepository.findByMatricule(matricule);
    }

    @Override
    public List<SageFemme> findSageFemmesByPosteSanteId(UUID posteSanteId) {
        return sageFemmeJpaRepository.findByPosteSanteId(posteSanteId);
    }

    @Override
    public void deleteById(UUID id) {
        utilisateurJpaRepository.deleteById(id);
    }
}
