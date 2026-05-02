package com.example.pfsagefemme.modules.consultation.domain.port.in;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;

import java.util.List;
import java.util.UUID;

public interface ListVisitesByPatienteUseCase {
    List<Visite> list(UUID patienteId);
}
