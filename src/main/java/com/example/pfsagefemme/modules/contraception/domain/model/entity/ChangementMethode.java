package com.example.pfsagefemme.modules.contraception.domain.model.entity;

import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.modules.user.domain.model.entity.SageFemme;
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

import java.time.LocalDate;

/**
 * Changement de methode contraceptive d'une patiente.
 * Source : Rapport - 5.2.1 Changement de methode (matrice DE -> VERS).
 * Sert a alimenter la matrice {@code LigneMatriceChangement} du rapport mensuel.
 */
@Entity
@Table(name = "changements_methode")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = {"patiente", "sageFemme"})
public class ChangementMethode extends BaseJpaEntity {

    @NotNull
    @Column(name = "date_changement", nullable = false)
    private LocalDate date;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ancienne_methode", nullable = false, length = 50)
    private MethodeContraceptiveEnum ancienneMethode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nouvelle_methode", nullable = false, length = 50)
    private MethodeContraceptiveEnum nouvelleMethode;

    @Column(name = "motif", columnDefinition = "TEXT")
    private String motif;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patiente_id", nullable = false)
    private Patiente patiente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sage_femme_id")
    private SageFemme sageFemme;
}
