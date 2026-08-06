package com.example.pfsagefemme.modules.user.infrastructure.persistence;

import com.example.pfsagefemme.modules.user.domain.model.entity.AdminPoste;
import com.example.pfsagefemme.modules.user.domain.model.entity.SageFemme;
import com.example.pfsagefemme.modules.user.domain.model.entity.Utilisateur;
import com.example.pfsagefemme.modules.user.domain.port.out.UtilisateurRepository;
import com.example.pfsagefemme.shared.domain.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
        Optional<AdminPoste> ap = utilisateurJpaRepository.findAdminPosteByIdWithPoste(id);
        if (ap.isPresent()) return ap.map(u -> u);
        Optional<SageFemme> sf = utilisateurJpaRepository.findSageFemmeByIdWithPoste(id);
        if (sf.isPresent()) return sf.map(u -> u);
        return utilisateurJpaRepository.findById(id);
    }

    @Override
    public Optional<Utilisateur> findByEmail(String email) {
        Optional<AdminPoste> ap = utilisateurJpaRepository.findAdminPosteByEmailWithPoste(email);
        if (ap.isPresent()) return ap.map(u -> u);
        Optional<SageFemme> sf = utilisateurJpaRepository.findSageFemmeByEmailWithPoste(email);
        if (sf.isPresent()) return sf.map(u -> u);
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
        List<Utilisateur> result = new ArrayList<>();
        result.addAll(utilisateurJpaRepository.findAllSuperAdmins());
        result.addAll(utilisateurJpaRepository.findAllAdminPostesWithPosteSante());
        result.addAll(utilisateurJpaRepository.findAllSagesFemmesWithPosteSante());
        return result;
    }

    @Override
    public List<Utilisateur> findByRole(RoleEnum role) {
        return switch (role) {
            case SUPER_ADMIN -> new ArrayList<>(utilisateurJpaRepository.findAllSuperAdmins());
            case ADMIN_POSTE -> new ArrayList<>(utilisateurJpaRepository.findAllAdminPostesWithPosteSante());
            case SAGE_FEMME -> new ArrayList<>(utilisateurJpaRepository.findAllSagesFemmesWithPosteSante());
        };
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
