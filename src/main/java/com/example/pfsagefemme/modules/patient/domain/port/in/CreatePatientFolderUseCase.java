package com.example.pfsagefemme.modules.patient.domain.port.in;

import com.example.pfsagefemme.modules.patient.domain.model.PatientFolder;
import com.example.pfsagefemme.modules.patient.domain.port.in.command.CreatePatientFolderCommand;

import java.util.UUID;

public interface CreatePatientFolderUseCase {
    /**
     * @param sageFemmeId id de la sage-femme connectee (responsable du dossier)
     */
    PatientFolder create(UUID sageFemmeId, CreatePatientFolderCommand command);
}
