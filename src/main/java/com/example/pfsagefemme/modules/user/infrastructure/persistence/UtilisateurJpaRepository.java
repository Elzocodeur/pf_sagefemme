package com.example.pfsagefemme.modules.user.infrastructure.persistence;

import com.example.pfsagefemme.modules.user.domain.model.entity.Utilisateur;
import com.example.pfsagefemme.shared.domain.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UtilisateurJpaRepository extends JpaRepository<Utilisateur, UUID> {

    Optional<Utilisateur> findByEmail(String email);

    Optional<Utilisateur> findByUsername(String username);

    @org.springframework.data.jpa.repository.Query("""
            SELECT u FROM Utilisateur u
            WHERE u.email = :id OR u.username = :id
            """)
    Optional<Utilisateur> findByEmailOrUsername(
            @org.springframework.data.repository.query.Param("id") String identifier);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<Utilisateur> findByRole(RoleEnum role);

    List<Utilisateur> findByActifTrue();
}
