package com.example.pfsagefemme.modules.adminposte.application.service;

import com.example.pfsagefemme.modules.adminposte.domain.port.in.GetRegistrePosteUseCase;
import com.example.pfsagefemme.modules.consultation.domain.model.entity.FicheConsultation;
import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.consultation.domain.port.out.FicheConsultationRepository;
import com.example.pfsagefemme.modules.consultation.domain.port.out.VisiteRepository;
import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.modules.patient.domain.port.out.PatienteRepository;
import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
import com.example.pfsagefemme.modules.postesante.domain.port.out.PosteSanteRepository;
import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import com.example.pfsagefemme.shared.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegistrePosteService implements GetRegistrePosteUseCase {

    private static final int RETARD_SUIVI_JOURS = 30;

    private final PatienteRepository patienteRepository;
    private final FicheConsultationRepository ficheRepository;
    private final VisiteRepository visiteRepository;
    private final PosteSanteRepository posteSanteRepository;

    @Override
    public RegistrePoste load(UUID posteSanteId, MethodeContraceptiveEnum methodeFilter) {
        PosteSante poste = posteSanteRepository.findById(posteSanteId)
                .orElseThrow(() -> new ResourceNotFoundException("Poste introuvable : " + posteSanteId));

        List<Patiente> toutes = patienteRepository.findByPosteSanteId(posteSanteId);

        List<PatienteRegistre> registre = toutes.stream()
                .filter(p -> methodeFilter == null || methodeMatchFilter(p, methodeFilter))
                .map(p -> buildRow(p))
                .toList();

        long totalActives = patienteRepository.countByStatut(StatutPatienteEnum.ACTIVE, posteSanteId);

        long avecRetard = registre.stream()
                // Meme definition que le dashboard sage-femme : patientes suivies dont le RDV est depasse
                .filter(r -> r.statut() == StatutPatienteEnum.ACTIVE || r.statut() == StatutPatienteEnum.NOUVELLE)
                .filter(r -> r.prochainRdv() != null && r.prochainRdv().isBefore(LocalDate.now()))
                .count();

        return new RegistrePoste(poste.getNom(), totalActives, registre, avecRetard, avecRetard);
    }

    private boolean methodeMatchFilter(Patiente p, MethodeContraceptiveEnum filtre) {
        FicheConsultation fiche = ficheRepository.findByPatienteId(p.getId()).orElse(null);
        if (fiche == null || fiche.getMethodeChoisie() == null) return false;
        return fiche.getMethodeChoisie() == filtre;
    }

    private PatienteRegistre buildRow(Patiente p) {
        FicheConsultation fiche = ficheRepository.findByPatienteId(p.getId()).orElse(null);
        MethodeContraceptiveEnum methode = fiche != null ? fiche.getMethodeChoisie() : null;
        String methodeLabel = buildMethodeLabel(methode);
        String sfNom = fiche != null && fiche.getSageFemme() != null
                ? "Mme. " + fiche.getSageFemme().getNom()
                : "—";

        Visite derniereVisite = visiteRepository.findLatestByPatienteId(p.getId()).orElse(null);
        LocalDate prochainRdv = derniereVisite != null ? derniereVisite.getDateProchainRdv() : null;

        return new PatienteRegistre(
                p.getId(),
                p.getNumeroDossier(),
                p.getPrenom(),
                p.getNom(),
                p.getAge(),
                sfNom,
                methode,
                methodeLabel,
                p.getStatutPatiente(),
                Boolean.TRUE.equals(p.getPvvih()),
                prochainRdv
        );
    }

    private String buildMethodeLabel(MethodeContraceptiveEnum m) {
        if (m == null) return "Non renseignée";
        return m.getSousCategorie().isBlank() ? m.getCategorie() : m.getSousCategorie();
    }
}
