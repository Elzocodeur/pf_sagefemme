package com.example.pfsagefemme.modules.adminposte.application.service;

import com.example.pfsagefemme.modules.adminposte.domain.port.in.GetMonPosteUseCase;
import com.example.pfsagefemme.modules.adminposte.domain.port.in.UpdateMonPosteUseCase;
import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
import com.example.pfsagefemme.modules.postesante.domain.port.out.PosteSanteRepository;
import com.example.pfsagefemme.shared.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MonPosteService implements GetMonPosteUseCase, UpdateMonPosteUseCase {

    private final PosteSanteRepository posteSanteRepository;

    @Override
    @Transactional(readOnly = true)
    public PosteSante load(UUID posteSanteId) {
        return posteSanteRepository.findById(posteSanteId)
                .orElseThrow(() -> new ResourceNotFoundException("Poste introuvable : " + posteSanteId));
    }

    @Override
    public PosteSante update(UUID posteSanteId, Command command) {
        PosteSante poste = posteSanteRepository.findById(posteSanteId)
                .orElseThrow(() -> new ResourceNotFoundException("Poste introuvable : " + posteSanteId));

        if (command.nom() != null && !command.nom().isBlank()) poste.setNom(command.nom());
        if (command.district() != null) poste.setDistrict(command.district());
        if (command.region() != null) poste.setRegion(command.region());
        if (command.responsableNom() != null) poste.setResponsableNom(command.responsableNom());
        if (command.telephone() != null) poste.setTelephone(command.telephone());
        if (command.responsableEmail() != null) poste.setResponsableEmail(command.responsableEmail());
        if (command.responsableTelephone() != null) poste.setResponsableTelephone(command.responsableTelephone());
        if (command.responsableTitre() != null) poste.setResponsableTitre(command.responsableTitre());

        return posteSanteRepository.save(poste);
    }
}
