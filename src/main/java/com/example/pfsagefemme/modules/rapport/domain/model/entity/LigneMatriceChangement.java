package com.example.pfsagefemme.modules.rapport.domain.model.entity;

import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.infrastructure.persistence.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Cellule de la matrice 5.2.1 Changement de methode (DE -> VERS).
 * methodeSource = ligne de la matrice ; methodeCible = colonne ; nombre = valeur.
 */
@Entity
@Table(name = "lignes_matrice_changement")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = "rapportMensuel")
public class LigneMatriceChangement extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rapport_mensuel_id", nullable = false)
    private RapportMensuel rapportMensuel;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "methode_source", nullable = false, length = 50)
    private MethodeContraceptiveEnum methodeSource;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "methode_cible", nullable = false, length = 50)
    private MethodeContraceptiveEnum methodeCible;

    @Column(name = "nombre", nullable = false)
    private Integer nombre;
}
