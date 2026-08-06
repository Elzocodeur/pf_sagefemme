package com.example.pfsagefemme.modules.user.infrastructure.persistence;

import com.example.pfsagefemme.modules.user.domain.model.entity.AdminPoste;
import com.example.pfsagefemme.modules.user.domain.model.entity.SageFemme;
import com.example.pfsagefemme.modules.user.domain.model.entity.SuperAdmin;
import com.example.pfsagefemme.modules.user.domain.model.entity.Utilisateur;
import com.example.pfsagefemme.shared.domain.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UtilisateurJpaRepository extends JpaRepository<Utilisateur, UUID> {

    Optional<Utilisateur> findByEmail(String email);

    Optional<Utilisateur> findByUsername(String username);

    @Query("""
            SELECT u FROM Utilisateur u
            WHERE u.email = :id OR u.username = :id
            """)
    Optional<Utilisateur> findByEmailOrUsername(@Param("id") String identifier);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<Utilisateur> findByRole(RoleEnum role);

    List<Utilisateur> findByActifTrue();

    @Query("SELECT sa FROM SuperAdmin sa")
    List<SuperAdmin> findAllSuperAdmins();

    @Query("SELECT ap FROM AdminPoste ap LEFT JOIN FETCH ap.posteSante")
    List<AdminPoste> findAllAdminPostesWithPosteSante();

    @Query("SELECT sf FROM SageFemme sf LEFT JOIN FETCH sf.posteSante")
    List<SageFemme> findAllSagesFemmesWithPosteSante();

    @Query("SELECT ap FROM AdminPoste ap LEFT JOIN FETCH ap.posteSante WHERE ap.email = :email")
    Optional<AdminPoste> findAdminPosteByEmailWithPoste(@Param("email") String email);

    @Query("SELECT sf FROM SageFemme sf LEFT JOIN FETCH sf.posteSante WHERE sf.email = :email")
    Optional<SageFemme> findSageFemmeByEmailWithPoste(@Param("email") String email);

    @Query("SELECT ap FROM AdminPoste ap LEFT JOIN FETCH ap.posteSante WHERE ap.id = :id")
    Optional<AdminPoste> findAdminPosteByIdWithPoste(@Param("id") UUID id);

    @Query("SELECT sf FROM SageFemme sf LEFT JOIN FETCH sf.posteSante WHERE sf.id = :id")
    Optional<SageFemme> findSageFemmeByIdWithPoste(@Param("id") UUID id);
}
