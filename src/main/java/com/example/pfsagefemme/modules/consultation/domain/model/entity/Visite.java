package com.example.pfsagefemme.modules.consultation.domain.model.entity;

import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.modules.user.domain.model.entity.SageFemme;
import com.example.pfsagefemme.shared.domain.enums.StatutVisiteEnum;
import com.example.pfsagefemme.shared.infrastructure.persistence.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Visite de suivi d'une patiente.
 * Source : Fiche de Consultation PF - Sheet "III - Visites" (tableau de suivi).
 */
@Entity
@Table(
        name = "visites",
        indexes = {
                @Index(name = "idx_visites_date", columnList = "date_visite"),
                @Index(name = "idx_visites_patiente", columnList = "patiente_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = {"patiente", "ficheConsultation", "sageFemme"})
public class Visite extends BaseJpaEntity {

    @Column(name = "date_visite", nullable = false)
    private LocalDate date;

    @Column(name = "date_dernieres_regles")
    private LocalDate dateDernieresRegles;

    @Column(name = "poids_kg")
    private Double poids;

    @Column(name = "tension_arterielle", length = 30)
    private String tensionArterielle;

    @Column(name = "observations", columnDefinition = "text")
    private String observations;

    @Column(name = "unites_fournies", length = 100)
    private String unitesFournies;

    @Column(name = "date_prochain_rdv")
    private LocalDate dateProchainRdv;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 20)
    private StatutVisiteEnum statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patiente_id", nullable = false)
    private Patiente patiente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fiche_consultation_id")
    private FicheConsultation ficheConsultation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sage_femme_id")
    private SageFemme sageFemme;
}
