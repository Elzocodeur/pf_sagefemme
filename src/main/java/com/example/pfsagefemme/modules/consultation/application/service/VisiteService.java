package com.example.pfsagefemme.modules.consultation.application.service;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.FicheConsultation;
import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.consultation.domain.port.in.CreateVisiteUseCase;
import com.example.pfsagefemme.modules.consultation.domain.port.in.ListVisitesByPatienteUseCase;
import com.example.pfsagefemme.modules.consultation.domain.port.in.ValidateVisiteUseCase;
import com.example.pfsagefemme.modules.consultation.domain.port.in.command.CreateVisiteCommand;
import com.example.pfsagefemme.modules.consultation.domain.port.out.FicheConsultationRepository;
import com.example.pfsagefemme.modules.consultation.domain.port.out.VisiteRepository;
import com.example.pfsagefemme.modules.contraception.domain.model.entity.ChangementMethode;
import com.example.pfsagefemme.modules.contraception.domain.port.out.ChangementMethodeRepository;
import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.modules.patient.domain.port.out.PatienteRepository;
import com.example.pfsagefemme.modules.user.domain.exception.UserNotFoundException;
import com.example.pfsagefemme.modules.user.domain.model.entity.SageFemme;
import com.example.pfsagefemme.modules.user.domain.port.out.UtilisateurRepository;
import com.example.pfsagefemme.shared.domain.enums.StatutVisiteEnum;
import com.example.pfsagefemme.shared.domain.exception.BusinessRuleException;
import com.example.pfsagefemme.shared.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Orchestration des visites de suivi.
 *
 * Cas couverts :
 *   - visite simple : on enregistre constantes + observations
 *   - visite avec changement de methode : on cree en plus un {@code ChangementMethode}
 *     et on met a jour la methode courante sur la {@code FicheConsultation} de la patiente
 *
 * Tout est dans une seule transaction - en cas d'echec aucune trace n'est laissee.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class VisiteService implements CreateVisiteUseCase, ListVisitesByPatienteUseCase, ValidateVisiteUseCase {

    private final VisiteRepository visiteRepository;
    private final FicheConsultationRepository ficheConsultationRepository;
    private final ChangementMethodeRepository changementMethodeRepository;
    private final PatienteRepository patienteRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Override
    public Visite create(CreateVisiteCommand cmd) {
        Patiente patiente = patienteRepository.findById(cmd.patienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Patiente introuvable : " + cmd.patienteId()));
        SageFemme sageFemme = resolveSageFemme(cmd.sageFemmeId());

        FicheConsultation fiche = ficheConsultationRepository.findByPatienteId(patiente.getId()).orElse(null);

        if (cmd.methodChange() != null) {
            applyMethodChange(patiente, sageFemme, fiche, cmd.methodChange(),
                    Optional.ofNullable(cmd.date()).orElseGet(LocalDate::now));
        }

        Visite visite = Visite.builder()
                .date(Optional.ofNullable(cmd.date()).orElseGet(LocalDate::now))
                .dateDernieresRegles(cmd.dateDernieresRegles())
                .poids(cmd.poids())
                .tensionArterielle(cmd.tensionArterielle())
                .observations(cmd.observations())
                .unitesFournies(cmd.unitesFournies())
                .dateProchainRdv(cmd.dateProchainRdv())
                .statut(Optional.ofNullable(cmd.statut()).orElse(StatutVisiteEnum.VALIDEE))
                .patiente(patiente)
                .ficheConsultation(fiche)
                .sageFemme(sageFemme)
                .build();
        return visiteRepository.save(visite);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Visite> list(UUID patienteId) {
        if (patienteRepository.findById(patienteId).isEmpty()) {
            throw new ResourceNotFoundException("Patiente introuvable : " + patienteId);
        }
        return visiteRepository.findByPatienteId(patienteId);
    }

    @Override
    public Visite validate(UUID visiteId) {
        Visite visite = visiteRepository.findById(visiteId)
                .orElseThrow(() -> new ResourceNotFoundException("Visite introuvable : " + visiteId));
        if (visite.getStatut() == StatutVisiteEnum.VALIDEE) {
            return visite; // idempotent
        }
        visite.setStatut(StatutVisiteEnum.VALIDEE);
        return visiteRepository.save(visite);
    }

    // ---- Helpers --------------------------------------------------------------

    private SageFemme resolveSageFemme(UUID id) {
        var user = utilisateurRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (!(user instanceof SageFemme sageFemme)) {
            throw new BusinessRuleException("Seule une sage-femme peut enregistrer une visite");
        }
        return sageFemme;
    }

    private void applyMethodChange(Patiente patiente,
                                   SageFemme sageFemme,
                                   FicheConsultation fiche,
                                   CreateVisiteCommand.MethodChange change,
                                   LocalDate date) {
        if (fiche == null || fiche.getMethodeChoisie() == null) {
            throw new BusinessRuleException(
                    "Impossible de changer de methode : aucune methode courante n'est enregistree pour la patiente " + patiente.getId()
            );
        }
        if (fiche.getMethodeChoisie() == change.nouvelleMethode()) {
            throw new BusinessRuleException("La nouvelle methode est identique a la methode courante");
        }

        ChangementMethode changement = ChangementMethode.builder()
                .date(date)
                .ancienneMethode(fiche.getMethodeChoisie())
                .nouvelleMethode(change.nouvelleMethode())
                .motif(change.motif())
                .patiente(patiente)
                .sageFemme(sageFemme)
                .build();
        changementMethodeRepository.save(changement);

        fiche.setMethodeChoisie(change.nouvelleMethode());
        ficheConsultationRepository.save(fiche);
    }
}
