package com.example.pfsagefemme.modules.user.domain.model.entity;

import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/** Sage-femme - executante des consultations PF. */
@Entity
@Table(name = "sages_femmes")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = "posteSante")
public class SageFemme extends Utilisateur {

    @Column(name = "matricule", unique = true, length = 50)
    private String matricule;

    @Column(name = "specialite", length = 100)
    private String specialite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poste_sante_id")
    private PosteSante posteSante;
}
