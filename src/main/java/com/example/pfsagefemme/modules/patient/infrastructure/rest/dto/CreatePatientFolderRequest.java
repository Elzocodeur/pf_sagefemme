package com.example.pfsagefemme.modules.patient.infrastructure.rest.dto;

import com.example.pfsagefemme.shared.domain.enums.EthnieEnum;
import com.example.pfsagefemme.shared.domain.enums.GenreDeVieEnum;
import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.NiveauInstructionEnum;
import com.example.pfsagefemme.shared.domain.enums.ProfessionMariEnum;
import com.example.pfsagefemme.shared.domain.enums.ReligionEnum;
import com.example.pfsagefemme.shared.domain.enums.SituationMatrimonialeEnum;
import com.example.pfsagefemme.shared.domain.enums.SourceInformationEnum;
import com.example.pfsagefemme.shared.domain.enums.StadeVihOmsEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

/**
 * Requete consolidee de creation d'un dossier patient.
 */
@Schema(description = "Creation complete d'un dossier patient (Patiente + FicheConsultation + 1ere Visite) en une seule operation")
public record CreatePatientFolderRequest(

        @Schema(description = "Numero de fiche (genere automatiquement si null)", example = "PF-042")
        @Size(max = 50)
        String numeroFiche,

        @Schema(description = "Date de la consultation initiale", example = "2024-05-24")
        @NotNull @PastOrPresent
        LocalDate dateConsultation,

        @Valid @NotNull
        IdentitySection identity,

        @Valid @NotNull
        ClinicalExamSection clinicalExam,

        @Valid
        TreatmentSection treatment
) {

    @Schema(description = "Etape 1 - Identite et statut social de la patiente")
    public record IdentitySection(
            @NotBlank @Size(max = 100) String prenom,
            @NotBlank @Size(max = 100) String nom,
            @Min(0) Integer age,
            @PastOrPresent LocalDate dateNaissance,
            @Size(max = 30) String telephone,
            @Size(max = 100) String profession,
            @Size(max = 255) String adresse,
            EthnieEnum ethnie,
            ReligionEnum religion,
            SituationMatrimonialeEnum situationMatrimoniale,
            NiveauInstructionEnum niveauInstruction,
            Set<GenreDeVieEnum> genreDeVie,
            ProfessionMariEnum professionMari,
            SourceInformationEnum sourceInformation,
            Boolean referee,
            @Size(max = 150) String origineReference,

            @Schema(description = "Statut serologique VIH")
            Boolean pvvih,

            @Schema(description = "Stade OMS - obligatoire si pvvih=true")
            StadeVihOmsEnum stadeOms,

            @Schema(description = "Sous traitement antiretroviral - obligatoire si pvvih=true")
            Boolean sousArv,

            @Schema(description = "Regime ARV (ex: TDF/3TC/DTG)", example = "TDF/3TC/DTG")
            @Size(max = 100) String traitementArv
    ) {}

    @Schema(description = "Etape 2 - Antecedents et examen clinique")
    public record ClinicalExamSection(
            @Min(0) Integer menarchesAge,
            @Min(0) Integer dureeRegles,
            @Min(0) Integer cycleMenstruel,
            LocalDate dateDernieresRegles,

            @Min(0) Integer enfantsVivants,
            @Min(0) Integer enfantsDecedes,
            @Min(0) Integer mortsNes,
            @Min(0) Integer avortements,
            @Size(max = 50) String ageDernierEnfant,
            @Size(max = 100) String allaitementExclusif,

            String antecedentsChirurgicaux,
            Boolean hypertensionArterielle,
            Boolean diabete,
            Boolean drepanocytose,

            String maladiesCardioVasculaires,
            String ictereMoinsUnAn,
            String migraines,
            String autresMedicaux,
            String plaintesActuelles,

            Double taille,
            Double poids,
            @Size(max = 255) String muqueuses,
            @Size(max = 255) String varices,
            @Size(max = 255) String oedemes,
            @Size(max = 255) String exophtalmie,
            Boolean allergieCuivre,

            @Size(max = 255) String thyroide,
            @Size(max = 255) String foie,
            @Size(max = 255) String ganglions,
            @Size(max = 255) String coeur,
            @Size(max = 255) String rate,
            @Size(max = 255) String poumons,
            @Size(max = 255) String peau,

            @Size(max = 255) String seins,
            @Size(max = 255) String vulve,
            @Size(max = 255) String perinee,
            @Size(max = 255) String vaginSpeculum,
            @Size(max = 255) String colSpeculum,
            @Size(max = 255) String colToucherVaginal,
            @Size(max = 255) String uterusToucherVaginal,
            @Size(max = 255) String annexesToucherVaginal,
            @Size(max = 255) String pertesToucherVaginal,

            String examensComplementaires
    ) {}

    @Schema(description = "Etape 3 - Methode contraceptive choisie + parametres de la 1ere visite")
    public record TreatmentSection(
            @NotNull MethodeContraceptiveEnum methodeChoisie,
            @NotNull LocalDate dateDebut,
            LocalDate dateProchainRdv,
            @Size(max = 100) String unitesFournies,
            String observations
    ) {}
}
