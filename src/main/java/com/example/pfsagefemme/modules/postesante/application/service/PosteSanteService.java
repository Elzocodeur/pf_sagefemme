package com.example.pfsagefemme.modules.postesante.application.service;

import com.example.pfsagefemme.modules.patient.domain.port.out.PatienteRepository;
import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
import com.example.pfsagefemme.modules.postesante.domain.port.in.CreatePosteSanteUseCase;
import com.example.pfsagefemme.modules.postesante.domain.port.in.GetPosteSanteUseCase;
import com.example.pfsagefemme.modules.postesante.domain.port.in.GetPostesSanteDashboardUseCase;
import com.example.pfsagefemme.modules.postesante.domain.port.in.ListPosteSantesUseCase;
import com.example.pfsagefemme.modules.postesante.domain.port.in.TogglePosteSanteUseCase;
import com.example.pfsagefemme.modules.postesante.domain.port.in.UpdatePosteSanteUseCase;
import com.example.pfsagefemme.modules.postesante.domain.port.in.command.CreatePosteSanteCommand;
import com.example.pfsagefemme.modules.postesante.domain.port.in.command.UpdatePosteSanteCommand;
import com.example.pfsagefemme.modules.postesante.domain.port.out.PosteSanteRepository;
import com.example.pfsagefemme.modules.user.domain.port.out.UtilisateurRepository;
import com.example.pfsagefemme.shared.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PosteSanteService implements
        CreatePosteSanteUseCase,
        ListPosteSantesUseCase,
        GetPosteSanteUseCase,
        UpdatePosteSanteUseCase,
        TogglePosteSanteUseCase,
        GetPostesSanteDashboardUseCase {

    private static final String IDENTIFIANT_PREFIX = "PS-SEN";

    private final PosteSanteRepository posteSanteRepository;
    private final PatienteRepository patienteRepository;
    private final UtilisateurRepository utilisateurRepository;

    // ====== CRUD =============================================================

    @Override
    public PosteSante create(CreatePosteSanteCommand cmd) {
        PosteSante poste = PosteSante.builder()
                .nom(cmd.nom())
                .adresse(cmd.adresse())
                .telephone(cmd.telephone())
                .district(cmd.district())
                .region(cmd.region())
                .responsableNom(cmd.responsableNom())
                .responsableTitre(cmd.responsableTitre())
                .responsableEmail(cmd.responsableEmail())
                .responsableTelephone(cmd.responsableTelephone())
                .actif(Optional.ofNullable(cmd.actif()).orElse(Boolean.TRUE))
                .identifiantExterne(generateIdentifiantExterne())
                .build();
        return posteSanteRepository.save(poste);
    }

    @Override
    public PosteSante update(UpdatePosteSanteCommand cmd) {
        PosteSante poste = posteSanteRepository.findById(cmd.id())
                .orElseThrow(() -> new ResourceNotFoundException("Poste introuvable : " + cmd.id()));
        if (cmd.nom() != null) poste.setNom(cmd.nom());
        if (cmd.adresse() != null) poste.setAdresse(cmd.adresse());
        if (cmd.telephone() != null) poste.setTelephone(cmd.telephone());
        if (cmd.district() != null) poste.setDistrict(cmd.district());
        if (cmd.region() != null) poste.setRegion(cmd.region());
        if (cmd.responsableNom() != null) poste.setResponsableNom(cmd.responsableNom());
        if (cmd.responsableTitre() != null) poste.setResponsableTitre(cmd.responsableTitre());
        if (cmd.responsableEmail() != null) poste.setResponsableEmail(cmd.responsableEmail());
        if (cmd.responsableTelephone() != null) poste.setResponsableTelephone(cmd.responsableTelephone());
        return posteSanteRepository.save(poste);
    }

    @Override
    public PosteSante activate(UUID id) {
        return toggleActif(id, true);
    }

    @Override
    public PosteSante deactivate(UUID id) {
        return toggleActif(id, false);
    }

    private PosteSante toggleActif(UUID id, boolean actif) {
        PosteSante poste = posteSanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Poste introuvable : " + id));
        poste.setActif(actif);
        return posteSanteRepository.save(poste);
    }

    // ====== Lecture ==========================================================

    @Override
    @Transactional(readOnly = true)
    public List<PosteSante> list(boolean onlyActive) {
        return onlyActive ? posteSanteRepository.findByActifTrue() : posteSanteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PosteSante byId(UUID id) {
        return posteSanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Poste de sante introuvable : " + id));
    }

    // ====== Dashboard ========================================================

    @Override
    @Transactional(readOnly = true)
    public PostesStats stats() {
        long postesActifs = posteSanteRepository.countActifs();
        long totalPatientes = patienteRepository.countAll(null);
        long totalSagesFemmes = utilisateurRepository.findByRole(
                com.example.pfsagefemme.shared.domain.enums.RoleEnum.SAGE_FEMME).size();
        long districts = posteSanteRepository.countDistinctDistricts();
        return new PostesStats(postesActifs, totalPatientes, totalSagesFemmes, districts);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PosteSanteRow> list(Boolean actif) {
        List<PosteSante> postes = (actif == null)
                ? posteSanteRepository.findAll()
                : (actif ? posteSanteRepository.findByActifTrue()
                         : posteSanteRepository.findAll().stream().filter(p -> !Boolean.TRUE.equals(p.getActif())).toList());

        return postes.stream()
                .map(p -> new PosteSanteRow(
                        p,
                        patienteRepository.countAll(p.getId()),
                        utilisateurRepository.findSageFemmesByPosteSanteId(p.getId()).size()
                ))
                .toList();
    }

    // ====== Helpers ==========================================================

    private String generateIdentifiantExterne() {
        int year = LocalDate.now().getYear();
        long count = posteSanteRepository.countByYear(year) + 1;
        return String.format("%s-%d-%03d", IDENTIFIANT_PREFIX, year, count);
    }
}
