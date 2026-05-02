package com.example.pfsagefemme.modules.user.domain.model.entity;

import com.example.pfsagefemme.shared.domain.enums.RoleEnum;
import com.example.pfsagefemme.shared.infrastructure.persistence.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Utilisateur abstrait - racine de la hierarchie {@link SuperAdmin},
 * {@link AdminPoste}, {@link SageFemme}.
 *
 * Strategie JPA : JOINED - chaque sous-classe a sa propre table reliee
 * a {@code utilisateurs} par cle primaire partagee.
 */
@Entity
@Table(name = "utilisateurs")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public abstract class Utilisateur extends BaseJpaEntity {

    @NotBlank
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @NotBlank
    @Column(name = "prenom", nullable = false, length = 100)
    private String prenom;

    @NotBlank
    @Email
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    /** Identifiant court de connexion (ex: chef_cs1, sf1) - facultatif, alternative a l'email. */
    @Column(name = "username", unique = true, length = 50)
    private String username;

    @NotBlank
    @Column(name = "mot_de_passe", nullable = false, length = 255)
    private String motDePasse;

    @Column(name = "telephone", length = 30)
    private String telephone;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 30)
    private RoleEnum role;

    @Column(name = "actif", nullable = false)
    private Boolean actif = Boolean.TRUE;
}
