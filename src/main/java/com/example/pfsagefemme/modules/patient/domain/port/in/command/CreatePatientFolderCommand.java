package com.example.pfsagefemme.modules.patient.domain.port.in.command;

import com.example.pfsagefemme.shared.domain.enums.EthnieEnum;
import com.example.pfsagefemme.shared.domain.enums.GenreDeVieEnum;
import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.NiveauInstructionEnum;
import com.example.pfsagefemme.shared.domain.enums.ProfessionMariEnum;
import com.example.pfsagefemme.shared.domain.enums.ReligionEnum;
import com.example.pfsagefemme.shared.domain.enums.SituationMatrimonialeEnum;
import com.example.pfsagefemme.shared.domain.enums.SourceInformationEnum;
import com.example.pfsagefemme.shared.domain.enums.StadeVihOmsEnum;

import java.time.LocalDate;
import java.util.Set;

/**
 * Commande de creation d'un dossier patient complet (Patiente + FicheConsultation + 1ere Visite).
 *
 * Structure miroir des 3 etapes du wizard frontend :
 *   - {@link IdentitySection} : etape 1 - etat civil
 *   - {@link ClinicalExamSection} : etape 2 - examen clinique
 *   - {@link TreatmentSection} : etape 3 - methode contraceptive choisie + 1ere visite
 *
 * Le numero de fiche peut etre null - le service le generera (ex: PF-042).
 */
public record CreatePatientFolderCommand(
        String numeroFiche,
        LocalDate dateConsultation,
        IdentitySection identity,
        ClinicalExamSection clinicalExam,
        TreatmentSection treatment
) {

    /** Etape 1 - etat civil de la patiente. */
    public record IdentitySection(
            String prenom,
            String nom,
            Integer age,
            LocalDate dateNaissance,
            String telephone,
            String profession,
            String adresse,
            EthnieEnum ethnie,
            ReligionEnum religion,
            SituationMatrimonialeEnum situationMatrimoniale,
            NiveauInstructionEnum niveauInstruction,
            Set<GenreDeVieEnum> genreDeVie,
            ProfessionMariEnum professionMari,
            SourceInformationEnum sourceInformation,
            Boolean referee,
            String origineReference,
            Boolean pvvih,
            StadeVihOmsEnum stadeOms,
            Boolean sousArv,
            String traitementArv
    ) {}

    /** Etape 2 - examen clinique (Sections II et III du formulaire papier). */
    public record ClinicalExamSection(
            // Antecedents gynecologiques
            Integer menarchesAge,
            Integer dureeRegles,
            Integer cycleMenstruel,
            LocalDate dateDernieresRegles,
            // Antecedents obstetricaux
            Integer enfantsVivants,
            Integer enfantsDecedes,
            Integer mortsNes,
            Integer avortements,
            String ageDernierEnfant,
            String allaitementExclusif,
            // Antecedents chirurgicaux + hereditaires
            String antecedentsChirurgicaux,
            Boolean hypertensionArterielle,
            Boolean diabete,
            Boolean drepanocytose,
            // Antecedents medicaux
            String maladiesCardioVasculaires,
            String ictereMoinsUnAn,
            String migraines,
            String autresMedicaux,
            String plaintesActuelles,
            // Examen general
            Double taille,
            Double poids,
            String muqueuses,
            String varices,
            String oedemes,
            String exophtalmie,
            Boolean allergieCuivre,
            // Examen physique
            String thyroide,
            String foie,
            String ganglions,
            String coeur,
            String rate,
            String poumons,
            String peau,
            // Examen gynecologique
            String seins,
            String vulve,
            String perinee,
            String vaginSpeculum,
            String colSpeculum,
            String colToucherVaginal,
            String uterusToucherVaginal,
            String annexesToucherVaginal,
            String pertesToucherVaginal,
            // Examens complementaires (Section III)
            String examensComplementaires
    ) {}

    /** Etape 3 - methode choisie + 1ere visite. */
    public record TreatmentSection(
            MethodeContraceptiveEnum methodeChoisie,
            LocalDate dateDebut,
            LocalDate dateProchainRdv,
            String unitesFournies,
            String observations
    ) {}
}
