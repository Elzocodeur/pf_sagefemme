package com.example.pfsagefemme.modules.patient.domain.model.entity;

import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
import com.example.pfsagefemme.modules.user.domain.model.entity.SageFemme;
import com.example.pfsagefemme.shared.domain.enums.EthnieEnum;
import com.example.pfsagefemme.shared.domain.enums.GenreDeVieEnum;
import com.example.pfsagefemme.shared.domain.enums.NiveauInstructionEnum;
import com.example.pfsagefemme.shared.domain.enums.ProfessionMariEnum;
import com.example.pfsagefemme.shared.domain.enums.ReligionEnum;
import com.example.pfsagefemme.shared.domain.enums.SituationMatrimonialeEnum;
import com.example.pfsagefemme.shared.domain.enums.SourceInformationEnum;
import com.example.pfsagefemme.shared.domain.enums.StadeVihOmsEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import com.example.pfsagefemme.shared.infrastructure.persistence.BaseJpaEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Patiente - sujet du dossier PF.
 * Source : Fiche de Consultation PF - Section I (Interrogatoire).
 */
@Entity
@Table(
        name = "patientes",
        indexes = {
                @Index(name = "idx_patientes_nom", columnList = "nom"),
                @Index(name = "idx_patientes_prenom", columnList = "prenom"),
                @Index(name = "idx_patientes_poste_sante", columnList = "poste_sante_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = {"posteSante", "sageFemme"})
public class Patiente extends BaseJpaEntity {

    @Column(name = "prenom", nullable = false, length = 100)
    private String prenom;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Min(0)
    @Column(name = "age")
    private Integer age;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "telephone", length = 30)
    private String telephone;

    @Column(name = "profession", length = 100)
    private String profession;

    @Column(name = "adresse", length = 255)
    private String adresse;

    @Enumerated(EnumType.STRING)
    @Column(name = "ethnie", length = 30)
    private EthnieEnum ethnie;

    @Enumerated(EnumType.STRING)
    @Column(name = "religion", length = 30)
    private ReligionEnum religion;

    @Enumerated(EnumType.STRING)
    @Column(name = "situation_matrimoniale", length = 30)
    private SituationMatrimonialeEnum situationMatrimoniale;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_instruction", length = 30)
    private NiveauInstructionEnum niveauInstruction;

    /** Genre de vie - multi-select (Tabac, Alcool, Autres). */
    @ElementCollection(targetClass = GenreDeVieEnum.class, fetch = FetchType.LAZY)
    @CollectionTable(
            name = "patientes_genre_de_vie",
            joinColumns = @JoinColumn(name = "patiente_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "genre_de_vie", length = 30)
    @Builder.Default
    private Set<GenreDeVieEnum> genreDeVie = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "profession_mari", length = 30)
    private ProfessionMariEnum professionMari;

    @Column(name = "referee")
    private Boolean referee;

    @Column(name = "origine_reference", length = 150)
    private String origineReference;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_information", length = 30)
    private SourceInformationEnum sourceInformation;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_patiente", length = 20)
    private StatutPatienteEnum statutPatiente;

    /** Numero de dossier humain (ex: PV-2024-042) - genere par le service. */
    @Column(name = "numero_dossier", length = 30, unique = true)
    private String numeroDossier;

    /** Seropositive - utilisee pour le tableau 5.2.3 (Actives PVVIH). */
    @Column(name = "pvvih")
    private Boolean pvvih;

    @Enumerated(EnumType.STRING)
    @Column(name = "stade_oms", length = 20)
    private StadeVihOmsEnum stadeOms;

    @Column(name = "sous_arv")
    private Boolean sousArv;

    /** Regime ARV (ex: TDF/3TC/DTG, TDF/3TC/EFV...). */
    @Column(name = "traitement_arv", length = 100)
    private String traitementArv;

    /** Motif d'arret renseigne quand statut = ARRETEE (ex: souhait grossesse). */
    @Column(name = "motif_arret", length = 255)
    private String motifArret;

    @Column(name = "date_arret")
    private LocalDate dateArret;

    @Column(name = "date_inscription")
    private LocalDate dateInscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poste_sante_id")
    private PosteSante posteSante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sage_femme_id")
    private SageFemme sageFemme;
}
