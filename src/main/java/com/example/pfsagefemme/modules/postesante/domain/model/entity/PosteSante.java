package com.example.pfsagefemme.modules.postesante.domain.model.entity;

import com.example.pfsagefemme.shared.infrastructure.persistence.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Poste de sante - lieu d'exercice des sages-femmes.
 * Source : organigramme du Service National de Sante de la Reproduction (Senegal).
 */
@Entity
@Table(name = "postes_sante")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class PosteSante extends BaseJpaEntity {

    @NotBlank
    @Column(name = "nom", nullable = false, length = 150)
    private String nom;

    @Column(name = "adresse", length = 255)
    private String adresse;

    @Column(name = "telephone", length = 30)
    private String telephone;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "actif", nullable = false)
    private Boolean actif = Boolean.TRUE;

    /** Identifiant externe humain (ex: PS-SEN-2024-082) - genere par le service. */
    @Column(name = "identifiant_externe", unique = true, length = 30)
    private String identifiantExterne;

    @Column(name = "responsable_nom", length = 150)
    private String responsableNom;

    @Column(name = "responsable_titre", length = 100)
    private String responsableTitre;

    @Column(name = "responsable_email", length = 150)
    private String responsableEmail;

    @Column(name = "responsable_telephone", length = 30)
    private String responsableTelephone;
}
