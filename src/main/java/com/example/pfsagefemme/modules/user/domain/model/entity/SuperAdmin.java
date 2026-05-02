package com.example.pfsagefemme.modules.user.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/** Administrateur national - portee transversale. */
@Entity
@Table(name = "super_admins")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class SuperAdmin extends Utilisateur {

    @Column(name = "niveau_acces", length = 50)
    private String niveauAcces;
}
