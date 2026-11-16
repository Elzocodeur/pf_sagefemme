package com.example.pfsagefemme.modules.patient.infrastructure.rest;

import com.example.pfsagefemme.modules.patient.domain.port.out.PatienteRepository;
import com.example.pfsagefemme.modules.user.infrastructure.security.PosteScope;
import com.example.pfsagefemme.shared.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/** Refuse l'acces a une patiente d'un autre poste (SAGE_FEMME / ADMIN_POSTE). */
@Component
@RequiredArgsConstructor
public class PatienteAccessGuard {

    private final PatienteRepository patienteRepository;
    private final PosteScope posteScope;

    @Transactional(readOnly = true)
    public void check(UUID patienteId) {
        var patiente = patienteRepository.findById(patienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Patiente introuvable : " + patienteId));
        var poste = patiente.getPosteSante();
        posteScope.checkAccess(poste == null ? null : poste.getId());
    }
}
