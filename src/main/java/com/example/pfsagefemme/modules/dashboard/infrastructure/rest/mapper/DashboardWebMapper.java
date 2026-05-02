package com.example.pfsagefemme.modules.dashboard.infrastructure.rest.mapper;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.dashboard.domain.port.in.GetSageFemmeDashboardUseCase;
import com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto.AbandonRelanceResponse;
import com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto.AbandonsRelanceResponse;
import com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto.ActiveRecenteResponse;
import com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto.ActivityDayResponse;
import com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto.ActivityResponse;
import com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto.SageFemmeDashboardResponse;
import com.example.pfsagefemme.modules.dashboard.infrastructure.rest.dto.StatCardResponse;
import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;

public final class DashboardWebMapper {

    private DashboardWebMapper() {}

    public static SageFemmeDashboardResponse toResponse(GetSageFemmeDashboardUseCase.SageFemmeDashboard d) {
        return new SageFemmeDashboardResponse(
                toResponse(d.total()),
                toResponse(d.actives()),
                toResponse(d.arretees()),
                toResponse(d.abandons()),
                toResponse(d.pvvih()),
                toResponse(d.activity()),
                toResponse(d.abandonsRelance()),
                d.activesRecentes().stream().map(DashboardWebMapper::toResponse).toList()
        );
    }

    private static StatCardResponse toResponse(GetSageFemmeDashboardUseCase.StatCard c) {
        return new StatCardResponse(c.value(), c.deltaPercent(), c.deltaLabel(), c.trend());
    }

    private static ActivityResponse toResponse(GetSageFemmeDashboardUseCase.Activity a) {
        return new ActivityResponse(
                a.total(),
                a.days().stream()
                        .map(d -> new ActivityDayResponse(d.date(), d.label(), d.count()))
                        .toList()
        );
    }

    private static AbandonsRelanceResponse toResponse(GetSageFemmeDashboardUseCase.AbandonsRelance ar) {
        return new AbandonsRelanceResponse(
                ar.thresholdDays(),
                ar.items().stream().map(DashboardWebMapper::toResponse).toList(),
                ar.totalEnRetard()
        );
    }

    private static AbandonRelanceResponse toResponse(GetSageFemmeDashboardUseCase.AbandonRelance ar) {
        Patiente p = ar.patiente();
        Visite v = ar.latestVisite();
        return new AbandonRelanceResponse(
                p.getId(),
                p.getNumeroDossier(),
                p.getPrenom(),
                p.getNom(),
                p.getTelephone(),
                v != null ? v.getDate() : p.getDateInscription(),
                ar.joursSansContact()
        );
    }

    private static ActiveRecenteResponse toResponse(GetSageFemmeDashboardUseCase.ActiveRecente ar) {
        Patiente p = ar.patiente();
        Visite v = ar.latestVisite();
        return new ActiveRecenteResponse(
                p.getId(),
                p.getNumeroDossier(),
                p.getPrenom(),
                p.getNom(),
                v == null ? null : v.getDate(),
                ar.statutClinique()
        );
    }
}
