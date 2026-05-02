package com.example.pfsagefemme.modules.postesante.infrastructure.rest.mapper;

import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
import com.example.pfsagefemme.modules.postesante.domain.port.in.GetPostesSanteDashboardUseCase;
import com.example.pfsagefemme.modules.postesante.domain.port.in.command.CreatePosteSanteCommand;
import com.example.pfsagefemme.modules.postesante.domain.port.in.command.UpdatePosteSanteCommand;
import com.example.pfsagefemme.modules.postesante.infrastructure.rest.dto.CreatePosteSanteRequest;
import com.example.pfsagefemme.modules.postesante.infrastructure.rest.dto.PosteSanteResponse;
import com.example.pfsagefemme.modules.postesante.infrastructure.rest.dto.PosteSanteRowResponse;
import com.example.pfsagefemme.modules.postesante.infrastructure.rest.dto.PostesStatsResponse;
import com.example.pfsagefemme.modules.postesante.infrastructure.rest.dto.UpdatePosteSanteRequest;

import java.util.UUID;

public final class PosteSanteWebMapper {

    private PosteSanteWebMapper() {}

    public static CreatePosteSanteCommand toCommand(CreatePosteSanteRequest req) {
        return new CreatePosteSanteCommand(
                req.nom(), req.adresse(), req.telephone(), req.district(), req.region(),
                req.responsableNom(), req.responsableTitre(), req.responsableEmail(), req.responsableTelephone(),
                req.actif()
        );
    }

    public static UpdatePosteSanteCommand toCommand(UUID id, UpdatePosteSanteRequest req) {
        return new UpdatePosteSanteCommand(
                id, req.nom(), req.adresse(), req.telephone(), req.district(), req.region(),
                req.responsableNom(), req.responsableTitre(), req.responsableEmail(), req.responsableTelephone()
        );
    }

    public static PosteSanteResponse toResponse(PosteSante p) {
        return new PosteSanteResponse(
                p.getId(), p.getIdentifiantExterne(), p.getNom(),
                p.getAdresse(), p.getTelephone(), p.getDistrict(), p.getRegion(), p.getActif(),
                p.getResponsableNom(), p.getResponsableTitre(), p.getResponsableEmail(), p.getResponsableTelephone()
        );
    }

    public static PosteSanteRowResponse toRow(GetPostesSanteDashboardUseCase.PosteSanteRow row) {
        PosteSante p = row.poste();
        return new PosteSanteRowResponse(
                p.getId(), p.getIdentifiantExterne(), p.getNom(),
                p.getDistrict(), p.getRegion(),
                row.nombrePatientes(), row.nombreSagesFemmes(), p.getActif()
        );
    }

    public static PostesStatsResponse toResponse(GetPostesSanteDashboardUseCase.PostesStats s) {
        return new PostesStatsResponse(
                s.postesActifs(), s.totalPatientes(), s.totalSagesFemmes(), s.districtsCouverts()
        );
    }
}
