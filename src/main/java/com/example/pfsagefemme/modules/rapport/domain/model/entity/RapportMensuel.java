package com.example.pfsagefemme.modules.rapport.domain.model.entity;

import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
import com.example.pfsagefemme.modules.user.domain.model.entity.SageFemme;
import com.example.pfsagefemme.shared.domain.enums.StatutRapportEnum;
import com.example.pfsagefemme.shared.infrastructure.persistence.BaseJpaEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Rapport mensuel de planification familiale.
 * Source : Rapport_Mensuel_PF.xlsx (en-tete + tableaux 5.2.1 a 5.2.4 + matrice changement).
 *
 * Compose de :
 *   - {@link LigneRapport} (cascade ALL) : lignes des tableaux 5.2.1 / 5.2.2 / 5.2.4
 *   - {@link LigneMatriceChangement} (cascade ALL) : cellules de la matrice 5.2.1
 *   - 6 champs scalaires {@code pvvihTranche*} : tableau 5.2.3 (1 seule ligne)
 */
@Entity
@Table(
        name = "rapports_mensuels",
        indexes = {
                @Index(name = "idx_rapports_mois", columnList = "mois"),
                @Index(name = "idx_rapports_annee", columnList = "annee"),
                @Index(name = "idx_rapports_poste", columnList = "poste_sante_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_rapport_mois_annee_sage_femme",
                        columnNames = {"mois", "annee", "sage_femme_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = {"posteSante", "sageFemme", "lignes", "matriceChangements"})
public class RapportMensuel extends BaseJpaEntity {

    @NotNull
    @Min(1)
    @Max(12)
    @Column(name = "mois", nullable = false)
    private Integer mois;

    @NotNull
    @Column(name = "annee", nullable = false)
    private Integer annee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poste_sante_id")
    private PosteSante posteSante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sage_femme_id")
    private SageFemme sageFemme;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 20)
    private StatutRapportEnum statut;

    @Column(name = "date_generation")
    private LocalDateTime dateGeneration;

    /** Lignes des tableaux 5.2.1 / 5.2.2 / 5.2.4 (composition - cascade ALL). */
    @OneToMany(
            mappedBy = "rapportMensuel",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<LigneRapport> lignes = new ArrayList<>();

    /** Matrice de changement de methode 5.2.1 (composition - cascade ALL). */
    @OneToMany(
            mappedBy = "rapportMensuel",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<LigneMatriceChangement> matriceChangements = new ArrayList<>();

    // ====== Tableau 5.2.3 - Actives PVVIH (PTME) ======
    @Column(name = "pvvih_tranche_10_14")
    private Integer pvvihTranche10_14;

    @Column(name = "pvvih_tranche_15_19")
    private Integer pvvihTranche15_19;

    @Column(name = "pvvih_tranche_20_24")
    private Integer pvvihTranche20_24;

    @Column(name = "pvvih_tranche_25_49")
    private Integer pvvihTranche25_49;

    @Column(name = "pvvih_tranche_50_plus")
    private Integer pvvihTranche50Plus;

    @Column(name = "pvvih_total")
    private Integer pvvihTotal;
}
