package com.example.pfsagefemme.modules.patient.application.service;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.FicheConsultation;
import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.consultation.domain.port.out.FicheConsultationRepository;
import com.example.pfsagefemme.modules.consultation.domain.port.out.VisiteRepository;
import com.example.pfsagefemme.modules.patient.domain.model.PatientFolder;
import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.modules.patient.domain.port.in.CreatePatientFolderUseCase;
import com.example.pfsagefemme.modules.patient.domain.port.in.GetPatientFolderUseCase;
import com.example.pfsagefemme.modules.patient.domain.port.in.SearchPatientsUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import com.example.pfsagefemme.modules.patient.domain.port.in.command.CreatePatientFolderCommand;
import com.example.pfsagefemme.modules.patient.domain.port.in.command.CreatePatientFolderCommand.ClinicalExamSection;
import com.example.pfsagefemme.modules.patient.domain.port.in.command.CreatePatientFolderCommand.IdentitySection;
import com.example.pfsagefemme.modules.patient.domain.port.in.command.CreatePatientFolderCommand.TreatmentSection;
import com.example.pfsagefemme.modules.patient.domain.port.out.PatienteRepository;
import com.example.pfsagefemme.modules.user.domain.exception.UserNotFoundException;
import com.example.pfsagefemme.modules.user.domain.model.entity.SageFemme;
import com.example.pfsagefemme.modules.user.domain.port.out.UtilisateurRepository;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutVisiteEnum;
import com.example.pfsagefemme.shared.domain.exception.BusinessRuleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Orchestrateur du dossier patient.
 *
 * Cree atomiquement (1 seule transaction) :
 *  1. la {@link Patiente}
 *  2. sa {@link FicheConsultation} (avec antecedents + methode choisie)
 *  3. sa 1ere {@link Visite}
 *
 * La sage-femme appelante (et donc son poste) est resolue depuis l'id passe par la couche web.
 * Toutes les references sont propagees automatiquement (poste, sage-femme).
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PatientFolderService implements
        CreatePatientFolderUseCase,
        GetPatientFolderUseCase,
        SearchPatientsUseCase {

    private final PatienteRepository patienteRepository;
    private final FicheConsultationRepository ficheConsultationRepository;
    private final VisiteRepository visiteRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Override
    public PatientFolder create(UUID sageFemmeId, CreatePatientFolderCommand command) {
        SageFemme sageFemme = resolveSageFemme(sageFemmeId);

        Patiente patiente = patienteRepository.save(buildPatiente(command.identity(), sageFemme));

        FicheConsultation fiche = ficheConsultationRepository.save(
                buildFiche(command, patiente, sageFemme)
        );

        Visite visite = visiteRepository.save(
                buildInitialVisite(command.treatment(), patiente, fiche, sageFemme, command.dateConsultation())
        );

        return new PatientFolder(patiente, fiche, List.of(visite));
    }

    @Override
    @Transactional(readOnly = true)
    public PatientFolder byPatienteId(UUID patienteId) {
        Patiente patiente = patienteRepository.findById(patienteId)
                .orElseThrow(() -> new com.example.pfsagefemme.shared.domain.exception.ResourceNotFoundException(
                        "Patiente introuvable : " + patienteId));
        FicheConsultation fiche = ficheConsultationRepository.findByPatienteId(patienteId).orElse(null);
        List<Visite> visites = visiteRepository.findByPatienteId(patienteId);
        return new PatientFolder(patiente, fiche, visites);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Patiente> search(String query, UUID posteSanteId, int page, int size) {
        String q = Optional.ofNullable(query).orElse("");
        PageRequest pageable = PageRequest.of(page, size, Sort.by("nom", "prenom"));
        return patienteRepository.searchPaged(q, posteSanteId, pageable);
    }

    // ---- Helpers de construction ---------------------------------------------------

    private SageFemme resolveSageFemme(UUID id) {
        var user = utilisateurRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (!(user instanceof SageFemme sageFemme) || sageFemme.getPosteSante() == null) {
            throw new BusinessRuleException("Seule une sage-femme rattachee a un poste peut creer une patiente");
        }
        return sageFemme;
    }

    private Patiente buildPatiente(IdentitySection s, SageFemme sageFemme) {
        return Patiente.builder()
                .prenom(s.prenom())
                .nom(s.nom())
                .age(s.age())
                .dateNaissance(s.dateNaissance())
                .telephone(s.telephone())
                .profession(s.profession())
                .adresse(s.adresse())
                .ethnie(s.ethnie())
                .religion(s.religion())
                .situationMatrimoniale(s.situationMatrimoniale())
                .niveauInstruction(s.niveauInstruction())
                .genreDeVie(s.genreDeVie() == null ? new HashSet<>() : new HashSet<>(s.genreDeVie()))
                .professionMari(s.professionMari())
                .sourceInformation(s.sourceInformation())
                .referee(s.referee())
                .origineReference(s.origineReference())
                .pvvih(s.pvvih())
                .stadeOms(s.stadeOms())
                .sousArv(s.sousArv())
                .traitementArv(s.traitementArv())
                .statutPatiente(StatutPatienteEnum.NOUVELLE)
                .numeroDossier(generateNumeroDossier())
                .dateInscription(LocalDate.now())
                .posteSante(sageFemme.getPosteSante())
                .sageFemme(sageFemme)
                .build();
    }

    private String generateNumeroDossier() {
        int year = LocalDate.now().getYear();
        long count = patienteRepository.countByYearOfDateInscription(year) + 1;
        return String.format("PV-%d-%03d", year, count);
    }

    private FicheConsultation buildFiche(CreatePatientFolderCommand cmd, Patiente patiente, SageFemme sageFemme) {
        ClinicalExamSection c = cmd.clinicalExam();
        TreatmentSection t = cmd.treatment();
        return FicheConsultation.builder()
                .numero(Optional.ofNullable(cmd.numeroFiche()).orElseGet(this::generateNumero))
                .dateConsultation(Optional.ofNullable(cmd.dateConsultation()).orElseGet(LocalDate::now))
                .patiente(patiente)
                .sageFemme(sageFemme)
                .posteSante(sageFemme.getPosteSante())
                // Section II-A
                .menarchesAge(c.menarchesAge())
                .dureeRegles(c.dureeRegles())
                .cycleMenstruel(c.cycleMenstruel())
                .dateDernieresRegles(c.dateDernieresRegles())
                // II-B
                .enfantsVivants(c.enfantsVivants())
                .enfantsDecedes(c.enfantsDecedes())
                .mortsNes(c.mortsNes())
                .avortements(c.avortements())
                .ageDernierEnfant(c.ageDernierEnfant())
                .allaitementExclusif(c.allaitementExclusif())
                // II-C, II-D
                .antecedentsChirurgicaux(c.antecedentsChirurgicaux())
                .hypertensionArterielle(c.hypertensionArterielle())
                .diabete(c.diabete())
                .drepanocytose(c.drepanocytose())
                // II-E
                .maladiesCardioVasculaires(c.maladiesCardioVasculaires())
                .ictereMoinsUnAn(c.ictereMoinsUnAn())
                .migraines(c.migraines())
                .autresMedicaux(c.autresMedicaux())
                .plaintesActuelles(c.plaintesActuelles())
                // Examen general
                .taille(c.taille())
                .poids(c.poids())
                .muqueuses(c.muqueuses())
                .varices(c.varices())
                .oedemes(c.oedemes())
                .exophtalmie(c.exophtalmie())
                .allergieCuivre(c.allergieCuivre())
                // Examen physique
                .thyroide(c.thyroide())
                .foie(c.foie())
                .ganglions(c.ganglions())
                .coeur(c.coeur())
                .rate(c.rate())
                .poumons(c.poumons())
                .peau(c.peau())
                // Examen gyneco
                .seins(c.seins())
                .vulve(c.vulve())
                .perinee(c.perinee())
                .vaginSpeculum(c.vaginSpeculum())
                .colSpeculum(c.colSpeculum())
                .colToucherVaginal(c.colToucherVaginal())
                .uterusToucherVaginal(c.uterusToucherVaginal())
                .annexesToucherVaginal(c.annexesToucherVaginal())
                .pertesToucherVaginal(c.pertesToucherVaginal())
                // Section III & IV
                .examensComplementaires(c.examensComplementaires())
                .methodeChoisie(t == null ? null : t.methodeChoisie())
                .build();
    }

    private Visite buildInitialVisite(TreatmentSection t, Patiente patiente, FicheConsultation fiche,
                                      SageFemme sageFemme, LocalDate dateConsultation) {
        if (t == null) {
            // pas d'etape traitement fournie - on cree quand meme une visite "ouverture de dossier"
            return Visite.builder()
                    .date(Optional.ofNullable(dateConsultation).orElseGet(LocalDate::now))
                    .patiente(patiente)
                    .ficheConsultation(fiche)
                    .sageFemme(sageFemme)
                    .statut(StatutVisiteEnum.VALIDEE)
                    .observations("Ouverture du dossier")
                    .build();
        }
        return Visite.builder()
                .date(Optional.ofNullable(t.dateDebut()).orElseGet(LocalDate::now))
                .patiente(patiente)
                .ficheConsultation(fiche)
                .sageFemme(sageFemme)
                .statut(StatutVisiteEnum.VALIDEE)
                .unitesFournies(t.unitesFournies())
                .dateProchainRdv(t.dateProchainRdv())
                .observations(t.observations())
                .build();
    }

    private String generateNumero() {
        // Numero simple base sur le timestamp - a remplacer par un compteur per-poste si besoin.
        return "PF-" + System.currentTimeMillis();
    }
}
