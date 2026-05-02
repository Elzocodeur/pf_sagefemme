package com.example.pfsagefemme.modules.user.domain.model.entity;

import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
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

/** Administrateur d'un poste de sante - portee locale. */
@Entity
@Table(name = "admin_postes")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = "posteSante")
public class AdminPoste extends Utilisateur {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poste_sante_id")
    private PosteSante posteSante;
}
