package com.example.pfsagefemme.modules.consultation.domain.model.entity;

import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Fiche de consultation - dossier clinique d'une patiente.
 * Source : Fiche de Consultation PF - Sections II (examens cliniques),
 * III (examens complementaires) et IV (methode appropriee choisie).
 */
@Entity
@Table(name = "fiches_consultation")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = {"patiente", "sageFemme", "posteSante"})
public class FicheConsultation extends BaseJpaEntity {

    @Column(name = "numero", nullable = false, unique = true, length = 50)
    private String numero;

    @Column(name = "date_consultation", nullable = false)
    private LocalDate dateConsultation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patiente_id", nullable = false, unique = true)
    private Patiente patiente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sage_femme_id")
    private SageFemme sageFemme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poste_sante_id")
    private PosteSante posteSante;

    // ====== Section II-A : Antecedents gynecologiques ======
    @Column(name = "menarches_age")
    private Integer menarchesAge;

    @Column(name = "duree_regles")
    private Integer dureeRegles;

    @Column(name = "cycle_menstruel")
    private Integer cycleMenstruel;

    @Column(name = "date_dernieres_regles")
    private LocalDate dateDernieresRegles;

    // ====== Section II-B : Antecedents obstetricaux ======
    @Column(name = "enfants_vivants")
    private Integer enfantsVivants;

    @Column(name = "enfants_decedes")
    private Integer enfantsDecedes;

    @Column(name = "morts_nes")
    private Integer mortsNes;

    @Column(name = "avortements")
    private Integer avortements;

    @Column(name = "age_dernier_enfant", length = 50)
    private String ageDernierEnfant;

    @Column(name = "allaitement_exclusif", length = 100)
    private String allaitementExclusif;

    // ====== Section II-C : Antecedents chirurgicaux ======
    @Column(name = "antecedents_chirurgicaux", columnDefinition = "TEXT")
    private String antecedentsChirurgicaux;

    // ====== Section II-D : Antecedents hereditaires ======
    @Column(name = "hypertension_arterielle")
    private Boolean hypertensionArterielle;

    @Column(name = "diabete")
    private Boolean diabete;

    @Column(name = "drepanocytose")
    private Boolean drepanocytose;

    // ====== Section II-E : Antecedents medicaux ======
    @Column(name = "maladies_cardio_vasculaires", columnDefinition = "TEXT")
    private String maladiesCardioVasculaires;

    @Column(name = "ictere_moins_un_an", columnDefinition = "TEXT")
    private String ictereMoinsUnAn;

    @Column(name = "migraines", columnDefinition = "TEXT")
    private String migraines;

    @Column(name = "autres_medicaux", columnDefinition = "TEXT")
    private String autresMedicaux;

    @Column(name = "plaintes_actuelles", columnDefinition = "TEXT")
    private String plaintesActuelles;

    // ====== Examen general ======
    @Column(name = "taille_cm")
    private Double taille;

    @Column(name = "poids_kg")
    private Double poids;

    @Column(name = "muqueuses", length = 255)
    private String muqueuses;

    @Column(name = "varices", length = 255)
    private String varices;

    @Column(name = "oedemes", length = 255)
    private String oedemes;

    @Column(name = "exophtalmie", length = 255)
    private String exophtalmie;

    @Column(name = "allergie_cuivre")
    private Boolean allergieCuivre;

    // ====== Examen physique ======
    @Column(name = "thyroide", length = 255)
    private String thyroide;

    @Column(name = "foie", length = 255)
    private String foie;

    @Column(name = "ganglions", length = 255)
    private String ganglions;

    @Column(name = "coeur", length = 255)
    private String coeur;

    @Column(name = "rate", length = 255)
    private String rate;

    @Column(name = "poumons", length = 255)
    private String poumons;

    @Column(name = "peau", length = 255)
    private String peau;

    // ====== Examen gynecologique ======
    @Column(name = "seins", length = 255)
    private String seins;

    @Column(name = "vulve", length = 255)
    private String vulve;

    @Column(name = "perinee", length = 255)
    private String perinee;

    @Column(name = "vagin_speculum", length = 255)
    private String vaginSpeculum;

    @Column(name = "col_speculum", length = 255)
    private String colSpeculum;

    @Column(name = "col_toucher_vaginal", length = 255)
    private String colToucherVaginal;

    @Column(name = "uterus_toucher_vaginal", length = 255)
    private String uterusToucherVaginal;

    @Column(name = "annexes_toucher_vaginal", length = 255)
    private String annexesToucherVaginal;

    @Column(name = "pertes_toucher_vaginal", length = 255)
    private String pertesToucherVaginal;

    // ====== Section III ======
    @Column(name = "examens_complementaires", columnDefinition = "TEXT")
    private String examensComplementaires;

    // ====== Section IV ======
    @Enumerated(EnumType.STRING)
    @Column(name = "methode_choisie", length = 50)
    private MethodeContraceptiveEnum methodeChoisie;
}
