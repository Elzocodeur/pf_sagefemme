package com.example.pfsagefemme.modules.patient.domain.port.in;

import com.example.pfsagefemme.modules.patient.domain.model.PatientFolder;

import java.util.UUID;

public interface GetPatientFolderUseCase {
    PatientFolder byPatienteId(UUID patienteId);
}
