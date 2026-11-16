package com.example.pfsagefemme.modules.patient.infrastructure.rest.mapper;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.FicheConsultation;
import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.patient.domain.model.PatientFolder;
import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.modules.patient.domain.port.in.command.CreatePatientFolderCommand;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.CreatePatientFolderRequest;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.FicheConsultationResponse;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.PatientFolderResponse;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.PatienteResponse;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.PatienteSummary;
import com.example.pfsagefemme.modules.patient.infrastructure.rest.dto.VisiteResponse;

import com.example.pfsagefemme.modules.contraception.domain.model.entity.ChangementMethode;
import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Conversion Web <-> Domaine pour le module patient.
 * Les mappers sont volontairement statiques (pas de Spring) pour rester simples et testables.
 */
public final class PatienteWebMapper {

    private PatienteWebMapper() {}

    // ====== Request -> Command =====================================================

    public static CreatePatientFolderCommand toCommand(CreatePatientFolderRequest req) {
        return new CreatePatientFolderCommand(
                req.numeroFiche(),
                req.dateConsultation(),
                toIdentitySection(req.identity()),
                toClinicalExamSection(req.clinicalExam()),
                toTreatmentSection(req.treatment())
        );
    }

    private static CreatePatientFolderCommand.IdentitySection toIdentitySection(CreatePatientFolderRequest.IdentitySection s) {
        return new CreatePatientFolderCommand.IdentitySection(
                s.prenom(), s.nom(), s.age(), s.dateNaissance(), s.telephone(),
                s.profession(), s.adresse(), s.ethnie(), s.religion(), s.situationMatrimoniale(),
                s.niveauInstruction(), s.genreDeVie(), s.professionMari(), s.sourceInformation(),
                s.referee(), s.origineReference(), s.pvvih(), s.stadeOms(), s.sousArv(),
                s.traitementArv()
        );
    }

    private static CreatePatientFolderCommand.ClinicalExamSection toClinicalExamSection(CreatePatientFolderRequest.ClinicalExamSection c) {
        return new CreatePatientFolderCommand.ClinicalExamSection(
                c.menarchesAge(), c.dureeRegles(), c.cycleMenstruel(), c.dateDernieresRegles(),
                c.enfantsVivants(), c.enfantsDecedes(), c.mortsNes(), c.avortements(),
                c.ageDernierEnfant(), c.allaitementExclusif(),
                c.antecedentsChirurgicaux(), c.hypertensionArterielle(), c.diabete(), c.drepanocytose(),
                c.maladiesCardioVasculaires(), c.ictereMoinsUnAn(), c.migraines(),
                c.autresMedicaux(), c.plaintesActuelles(),
                c.taille(), c.poids(), c.muqueuses(), c.varices(), c.oedemes(),
                c.exophtalmie(), c.allergieCuivre(),
                c.thyroide(), c.foie(), c.ganglions(), c.coeur(), c.rate(), c.poumons(), c.peau(),
                c.seins(), c.vulve(), c.perinee(), c.vaginSpeculum(), c.colSpeculum(),
                c.colToucherVaginal(), c.uterusToucherVaginal(), c.annexesToucherVaginal(), c.pertesToucherVaginal(),
                c.examensComplementaires()
        );
    }

    private static CreatePatientFolderCommand.TreatmentSection toTreatmentSection(CreatePatientFolderRequest.TreatmentSection t) {
        if (t == null) return null;
        return new CreatePatientFolderCommand.TreatmentSection(
                t.methodeChoisie(), t.dateDebut(), t.dateProchainRdv(), t.unitesFournies(), t.observations()
        );
    }

    // ====== Domain -> Response =====================================================

    public static PatienteResponse toResponse(Patiente p) {
        Set<com.example.pfsagefemme.shared.domain.enums.GenreDeVieEnum> genreDeVie =
                p.getGenreDeVie() != null ? new HashSet<>(p.getGenreDeVie()) : Set.of();
        return new PatienteResponse(
                p.getId(), p.getNumeroDossier(), p.getPrenom(), p.getNom(), p.getAge(), p.getDateNaissance(),
                p.getTelephone(), p.getProfession(), p.getAdresse(), p.getEthnie(), p.getReligion(),
                p.getSituationMatrimoniale(), p.getNiveauInstruction(), genreDeVie,
                p.getProfessionMari(), p.getSourceInformation(), p.getReferee(), p.getOrigineReference(),
                p.getPvvih(), p.getStadeOms(), p.getSousArv(), p.getTraitementArv(),
                p.getStatutPatiente(), p.getMotifArret(), p.getDateArret(), p.getDateInscription(),
                p.getPosteSante() == null ? null : p.getPosteSante().getId(),
                p.getPosteSante() == null ? null : p.getPosteSante().getNom(),
                p.getSageFemme() == null ? null : p.getSageFemme().getId()
        );
    }

    public static PatienteSummary toSummary(Patiente p, MethodeContraceptiveEnum methode) {
        return toSummary(p, methode, List.of());
    }

    /** Variante enrichie : expose le dernier changement de methode pour l'affichage en liste. */
    public static PatienteSummary toSummary(Patiente p, MethodeContraceptiveEnum methode,
                                            List<ChangementMethode> changements) {
        ChangementMethode dernier = changements.stream()
                .max(java.util.Comparator.comparing(ChangementMethode::getDate))
                .orElse(null);
        MethodeContraceptiveEnum ancienne = dernier == null ? null : dernier.getAncienneMethode();
        return new PatienteSummary(
                p.getId(), p.getNumeroDossier(), p.getPrenom(), p.getNom(), p.getAge(),
                p.getStatutPatiente(), p.getPvvih(), p.getDateInscription(),
                methode, methodeLabel(methode),
                p.getPosteSante() == null ? null : p.getPosteSante().getNom(),
                changements.size(),
                ancienne, methodeLabel(ancienne),
                dernier == null ? null : dernier.getDate(),
                dernier == null ? null : dernier.getMotif()
        );
    }

    public static String methodeLabel(MethodeContraceptiveEnum m) {
        if (m == null) return null;
        return m.getSousCategorie().isBlank() ? m.getCategorie() : m.getCategorie() + " " + m.getSousCategorie();
    }

    public static FicheConsultationResponse toResponse(FicheConsultation f) {
        return new FicheConsultationResponse(
                f.getId(), f.getNumero(), f.getDateConsultation(),
                f.getPatiente() == null ? null : f.getPatiente().getId(),
                f.getSageFemme() == null ? null : f.getSageFemme().getId(),
                f.getPosteSante() == null ? null : f.getPosteSante().getId(),
                f.getMenarchesAge(), f.getDureeRegles(), f.getCycleMenstruel(), f.getDateDernieresRegles(),
                f.getEnfantsVivants(), f.getEnfantsDecedes(), f.getAvortements(),
                f.getMortsNes(), f.getAgeDernierEnfant(), f.getAllaitementExclusif(),
                f.getTaille(), f.getPoids(),
                f.getHypertensionArterielle(), f.getDiabete(), f.getDrepanocytose(), f.getAllergieCuivre(),
                f.getPlaintesActuelles(), f.getExamensComplementaires(), f.getMethodeChoisie()
        );
    }

    public static VisiteResponse toResponse(Visite v) {
        return new VisiteResponse(
                v.getId(), v.getDate(), v.getPoids(), v.getTensionArterielle(),
                v.getObservations(), v.getUnitesFournies(), v.getDateProchainRdv(),
                v.getSageFemme() == null ? null : v.getSageFemme().getId()
        );
    }

    public static PatientFolderResponse toResponse(PatientFolder folder) {
        return new PatientFolderResponse(
                toResponse(folder.patiente()),
                Optional.ofNullable(folder.ficheConsultation()).map(PatienteWebMapper::toResponse).orElse(null),
                folder.visites() == null ? List.of() : folder.visites().stream().map(PatienteWebMapper::toResponse).toList()
        );
    }
}
