package com.example.pfsagefemme.modules.adminposte.domain.port.in;

import com.example.pfsagefemme.shared.domain.enums.AgeBracketEnum;
import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;

import java.util.Map;
import java.util.UUID;
import java.util.List;

/** Ecran "Rapport mensuel PF" agrege pour un poste (chef de poste). */
public interface GetRapportPosteUseCase {

    record KpiRapport(long value, String label, Double evolutionPercent) {}

    record MethodeLine(
            MethodeContraceptiveEnum methode,
            String methodeLabel,
            Map<AgeBracketEnum, Long> tranches,
            long total
    ) {}

    record RapportPosteData(
            int mois,
            int annee,
            String periodeLabel,
            String posteNom,
            String district,
            KpiRapport nouvellesPv,
            KpiRapport anciennesPv,
            KpiRapport actives,
            KpiRapport inactives,
            KpiRapport pvvih,
            List<MethodeLine> section521,
            List<MethodeLine> section522,
            List<MethodeLine> section524
    ) {}

    RapportPosteData load(int mois, int annee, UUID posteSanteId);
}
