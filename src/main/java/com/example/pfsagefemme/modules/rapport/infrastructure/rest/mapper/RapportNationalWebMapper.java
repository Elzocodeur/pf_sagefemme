package com.example.pfsagefemme.modules.rapport.infrastructure.rest.mapper;

import com.example.pfsagefemme.modules.rapport.domain.model.RapportNationalData;
import com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto.MethodeQuantiteResponse;
import com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto.RapportNationalPosteRowResponse;
import com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto.RapportNationalResponse;
import com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto.RapportNationalStatsResponse;
import com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto.RapportNationalTotalResponse;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public final class RapportNationalWebMapper {

    private RapportNationalWebMapper() {}

    public static RapportNationalResponse toResponse(RapportNationalData d) {
        return new RapportNationalResponse(
                d.mois(),
                d.annee(),
                periodeLabel(d.mois(), d.annee()),
                toResponse(d.stats()),
                d.postesRows().stream().map(RapportNationalWebMapper::toResponse).toList(),
                toResponse(d.totalNational()),
                d.larcDetail().stream().map(RapportNationalWebMapper::toResponse).toList(),
                d.hormonauxConsommes().stream().map(RapportNationalWebMapper::toResponse).toList()
        );
    }

    private static String periodeLabel(int mois, int annee) {
        String name = Month.of(mois).getDisplayName(TextStyle.FULL_STANDALONE, Locale.FRENCH);
        return name.substring(0, 1).toUpperCase() + name.substring(1) + " " + annee;
    }

    private static RapportNationalStatsResponse toResponse(RapportNationalData.Stats s) {
        return new RapportNationalStatsResponse(
                s.nouvellesPv(), s.deltaNouvellesPercent(), s.anciennesPv(),
                s.actives(), s.inactives(), s.pvvihActives()
        );
    }

    private static RapportNationalPosteRowResponse toResponse(RapportNationalData.PosteRow row) {
        return new RapportNationalPosteRowResponse(
                row.poste().getId(), row.poste().getNom(),
                row.nouvellesPv(), row.anciennesPv(), row.actives(), row.inactives(), row.pvvih(), row.total()
        );
    }

    private static RapportNationalTotalResponse toResponse(RapportNationalData.TotalRow t) {
        return new RapportNationalTotalResponse(
                t.nouvellesPv(), t.anciennesPv(), t.actives(), t.inactives(), t.pvvih(), t.total()
        );
    }

    private static MethodeQuantiteResponse toResponse(RapportNationalData.MethodeQuantite mq) {
        return new MethodeQuantiteResponse(mq.methode(), mq.label(), mq.quantite());
    }
}
