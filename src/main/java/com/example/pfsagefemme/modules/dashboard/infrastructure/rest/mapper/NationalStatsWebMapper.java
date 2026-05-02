package com.example.pfsagefemme.modules.dashboard.infrastructure.rest.mapper;

import com.example.pfsagefemme.modules.dashboard.domain.port.in.GetNationalStatsUseCase;
import com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto.NationalGlobalStatsResponse;
import com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto.NationalStatsResponse;
import com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto.PosteStatsRowResponse;
import com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto.RecentPatientResponse;
import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;

public final class NationalStatsWebMapper {

    private NationalStatsWebMapper() {}

    public static NationalStatsResponse toResponse(GetNationalStatsUseCase.NationalStats s) {
        return new NationalStatsResponse(
                toResponse(s.global()),
                s.postesRows().stream().map(NationalStatsWebMapper::toResponse).toList(),
                s.totalNational(),
                s.tauxMoyenPercent(),
                s.recentPatients().stream().map(NationalStatsWebMapper::toResponse).toList()
        );
    }

    private static NationalGlobalStatsResponse toResponse(GetNationalStatsUseCase.GlobalStats g) {
        return new NationalGlobalStatsResponse(
                g.totalPatientes(), g.actives(), g.abandons(), g.pvvih(), g.sagesFemmes()
        );
    }

    private static PosteStatsRowResponse toResponse(GetNationalStatsUseCase.PosteRow row) {
        PosteSante p = row.poste();
        return new PosteStatsRowResponse(
                p.getId(), p.getNom(), p.getDistrict(), p.getRegion(),
                row.total(), row.actives(), row.abandons(), row.pvvih(), row.sagesFemmes(),
                row.tauxActivitePercent()
        );
    }

    private static RecentPatientResponse toResponse(Patiente p) {
        return new RecentPatientResponse(
                p.getId(),
                p.getNumeroDossier(),
                p.getPosteSante() == null ? null : p.getPosteSante().getRegion(),
                p.getPosteSante() == null ? null : p.getPosteSante().getNom(),
                p.getDateInscription()
        );
    }
}
