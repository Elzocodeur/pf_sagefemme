package com.example.pfsagefemme.modules.patient.domain.port.in;

import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface SearchPatientsUseCase {
  Page<Patiente> search(String query, UUID posteSanteId, int page, int size);
}
