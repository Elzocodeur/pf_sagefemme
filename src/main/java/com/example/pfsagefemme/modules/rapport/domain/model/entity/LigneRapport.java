package com.example.pfsagefemme.modules.rapport.domain.model.entity;

import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.TypeRapportEnum;
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
 * Ligne d'un des tableaux 5.2.1 / 5.2.2 / 5.2.4 du rapport mensuel.
 * Une ligne = une methode contraceptive + 5 tranches d'age + un total.
 */
@Entity
@Table(name = "lignes_rapport")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = "rapportMensuel")
public class LigneRapport extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rapport_mensuel_id", nullable = false)
    private RapportMensuel rapportMensuel;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_rapport", nullable = false, length = 30)
    private TypeRapportEnum typeRapport;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "methode", nullable = false, length = 50)
    private MethodeContraceptiveEnum methode;

    @Column(name = "tranche_10_14")
    private Integer tranche10_14;

    @Column(name = "tranche_15_19")
    private Integer tranche15_19;

    @Column(name = "tranche_20_24")
    private Integer tranche20_24;

    @Column(name = "tranche_25_49")
    private Integer tranche25_49;

    @Column(name = "tranche_50_plus")
    private Integer tranche50Plus;

    @Column(name = "total")
    private Integer total;
}
