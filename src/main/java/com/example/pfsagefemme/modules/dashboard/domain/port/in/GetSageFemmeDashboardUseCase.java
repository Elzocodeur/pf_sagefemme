package com.example.pfsagefemme.modules.dashboard.domain.port.in;

import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.shared.domain.enums.StatutCliniqueEnum;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface GetSageFemmeDashboardUseCase {

    /** Tendance affichee sous chaque KPI. */
    enum Trend { UP, DOWN, STABLE, ALERT, PRIORITY }

    record StatCard(long value, Double deltaPercent, String deltaLabel, Trend trend) {}

    record ActivityDay(LocalDate date, String label, long count) {}

    record Activity(long total, List<ActivityDay> days) {}

    record AbandonRelance(Patiente patiente, Visite latestVisite, long joursSansContact) {}

    record AbandonsRelance(int thresholdDays, List<AbandonRelance> items, long totalEnRetard) {}

    record ActiveRecente(Patiente patiente, Visite latestVisite, StatutCliniqueEnum statutClinique,
                         com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum methode) {}

    record SageFemmeDashboard(
            StatCard total,
            StatCard actives,
            StatCard arretees,
            StatCard abandons,
            StatCard pvvih,
            Activity activity,
            AbandonsRelance abandonsRelance,
            List<ActiveRecente> activesRecentes
    ) {}

    SageFemmeDashboard load(UUID posteSanteId);
}
