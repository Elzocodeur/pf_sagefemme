package com.example.pfsagefemme.modules.adminposte.application.service;

import com.example.pfsagefemme.modules.adminposte.domain.port.in.GetEquipePosteUseCase;
import com.example.pfsagefemme.modules.consultation.domain.port.out.VisiteRepository;
import com.example.pfsagefemme.modules.patient.domain.port.out.PatienteRepository;
import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
import com.example.pfsagefemme.modules.postesante.domain.port.out.PosteSanteRepository;
import com.example.pfsagefemme.modules.rapport.domain.model.entity.RapportMensuel;
import com.example.pfsagefemme.modules.rapport.domain.port.out.RapportMensuelRepository;
import com.example.pfsagefemme.modules.user.domain.model.entity.SageFemme;
import com.example.pfsagefemme.modules.user.domain.port.out.UtilisateurRepository;
import com.example.pfsagefemme.shared.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EquipePosteService implements GetEquipePosteUseCase {

    private final PosteSanteRepository posteSanteRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PatienteRepository patienteRepository;
    private final VisiteRepository visiteRepository;
    private final RapportMensuelRepository rapportMensuelRepository;

    @Override
    public EquipePoste load(UUID posteSanteId) {
        PosteSante poste = posteSanteRepository.findById(posteSanteId)
                .orElseThrow(() -> new ResourceNotFoundException("Poste introuvable : " + posteSanteId));

        List<SageFemme> sagesFemmes = utilisateurRepository.findSageFemmesByPosteSanteId(posteSanteId);
        List<RapportMensuel> rapportsPoste = rapportMensuelRepository.findByPosteSanteId(posteSanteId);

        LocalDate dernierRapportDate = rapportsPoste.stream()
                .filter(r -> r.getDateGeneration() != null)
                .map(r -> r.getDateGeneration().toLocalDate())
                .max(Comparator.naturalOrder())
                .orElse(null);

        List<SageFemmeWithStats> rows = sagesFemmes.stream()
                .map(sf -> buildRow(sf))
                .toList();

        int total = rows.size();
        double moyenne = total == 0 ? 0.0 :
                rows.stream().mapToInt(SageFemmeWithStats::nbPatientes).sum() / (double) total;

        boolean conformite = rows.stream().allMatch(r -> r.nbPatientes() > 0);

        return new EquipePoste(poste.getNom(), total, rows, moyenne, dernierRapportDate, conformite);
    }

    private SageFemmeWithStats buildRow(SageFemme sf) {
        int nbPatientes = patienteRepository.findBySageFemmeId(sf.getId()).size();

        LocalDate derniereActivite = visiteRepository
                .findBySageFemmeIdAndDateBetween(sf.getId(),
                        LocalDate.now().minusDays(90), LocalDate.now())
                .stream()
                .map(v -> v.getDate())
                .max(Comparator.naturalOrder())
                .orElse(null);

        return new SageFemmeWithStats(
                sf.getId(),
                sf.getMatricule(),
                sf.getPrenom(),
                sf.getNom(),
                sf.getUsername(),
                Boolean.TRUE.equals(sf.getActif()),
                nbPatientes,
                derniereActivite
        );
    }
}
