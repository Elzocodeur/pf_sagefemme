package com.example.pfsagefemme.modules.consultation.infrastructure.rest.mapper;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.consultation.domain.port.in.command.CreateVisiteCommand;
import com.example.pfsagefemme.modules.consultation.infrastructure.rest.dto.CreateVisiteRequest;
import com.example.pfsagefemme.modules.consultation.infrastructure.rest.dto.VisiteDetailResponse;

import java.util.UUID;

public final class VisiteWebMapper {

    private VisiteWebMapper() {}

    public static CreateVisiteCommand toCommand(UUID patienteId, UUID sageFemmeId, CreateVisiteRequest req) {
        CreateVisiteCommand.MethodChange methodChange = req.methodChange() == null
                ? null
                : new CreateVisiteCommand.MethodChange(req.methodChange().nouvelleMethode(), req.methodChange().motif());
        return new CreateVisiteCommand(
                patienteId,
                sageFemmeId,
                req.date(),
                req.dateDernieresRegles(),
                req.poids(),
                req.tensionArterielle(),
                req.observations(),
                req.unitesFournies(),
                req.dateProchainRdv(),
                req.statut(),
                methodChange
        );
    }

    public static VisiteDetailResponse toResponse(Visite v) {
        return new VisiteDetailResponse(
                v.getId(),
                v.getPatiente() == null ? null : v.getPatiente().getId(),
                v.getFicheConsultation() == null ? null : v.getFicheConsultation().getId(),
                v.getSageFemme() == null ? null : v.getSageFemme().getId(),
                v.getDate(),
                v.getDateDernieresRegles(),
                v.getPoids(),
                v.getTensionArterielle(),
                v.getObservations(),
                v.getUnitesFournies(),
                v.getDateProchainRdv(),
                v.getStatut()
        );
    }
}
