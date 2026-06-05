package com.example.pfsagefemme.modules.adminposte.domain.port.in;

import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/** Ecran "Statistiques poste / Vue generale" du chef de poste. */
public interface GetStatsPosteUseCase {

    record KpiCard(long value, String label, String description) {}

    record ActiveRecentePoste(
            UUID id,
            String numeroDossier,
            String prenom,
            String nom,
            MethodeContraceptiveEnum methode,
            String methodeLabel,
            StatutPatienteEnum statut
    ) {}

    record ASurveillerItem(
            String type,
            UUID patienteId,
            String prenom,
            String nom,
            String message
    ) {}

    record MethodeCount(
            MethodeContraceptiveEnum methode,
            String categorie,
            long count
    ) {}

    record StatsPoste(
            KpiCard total,
            KpiCard actives,
            KpiCard arretees,
            KpiCard abandons,
            KpiCard pvvih,
            List<ActiveRecentePoste> activesRecentes,
            List<ASurveillerItem> aSurveiller,
            List<MethodeCount> repartitionParMethode,
            long tauxRetentionPercent,
            String methodePlusSollicitee,
            double evolutionMensuellePercent
    ) {}

    StatsPoste load(UUID posteSanteId);
}
