package com.example.pfsagefemme.modules.rapport.infrastructure.rest.mapper;

import com.example.pfsagefemme.modules.rapport.domain.model.RapportData;
import com.example.pfsagefemme.modules.rapport.domain.model.entity.RapportMensuel;
import com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto.MatriceCellResponse;
import com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto.MethodeLineResponse;
import com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto.PvvihSectionResponse;
import com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto.RapportMensuelResponse;
import com.example.pfsagefemme.modules.rapport.infrastructure.rest.dto.RapportStatsResponse;
import com.example.pfsagefemme.modules.user.domain.model.entity.SageFemme;
import com.example.pfsagefemme.shared.domain.enums.AgeBracketEnum;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public final class RapportMensuelWebMapper {

    private RapportMensuelWebMapper() {}

    public static RapportMensuelResponse toResponse(RapportData data) {
        RapportMensuel r = data.rapport();
        SageFemme sf = (SageFemme) r.getSageFemme();
        String signedBy = sf == null ? null : (sf.getPrenom() + " " + sf.getNom() + " (SFE)");

        return new RapportMensuelResponse(
                r.getId(),
                r.getMois(),
                r.getAnnee(),
                periodeLabel(r.getMois(), r.getAnnee()),
                r.getPosteSante() == null ? null : r.getPosteSante().getId(),
                r.getSageFemme() == null ? null : r.getSageFemme().getId(),
                signedBy,
                r.getStatut(),
                r.getDateGeneration(),
                toResponse(data.stats()),
                toResponseList(data.section521()),
                toResponseList(data.section521Bis()),
                toResponseList(data.section522()),
                toResponse(data.section523()),
                toResponseList(data.section524()),
                data.matriceChangements().stream().map(RapportMensuelWebMapper::toResponse).toList()
        );
    }

    private static String periodeLabel(int mois, int annee) {
        return Month.of(mois).getDisplayName(TextStyle.FULL_STANDALONE, Locale.FRENCH)
                .substring(0, 1).toUpperCase()
                + Month.of(mois).getDisplayName(TextStyle.FULL_STANDALONE, Locale.FRENCH).substring(1)
                + " " + annee;
    }

    private static RapportStatsResponse toResponse(RapportData.Stats s) {
        return new RapportStatsResponse(s.nouvellesPv(), s.anciennesPv(), s.actives(), s.inactives(), s.pvvihActives());
    }

    private static List<MethodeLineResponse> toResponseList(List<RapportData.MethodeLine> lines) {
        return lines.stream().map(RapportMensuelWebMapper::toResponse).toList();
    }

    private static MethodeLineResponse toResponse(RapportData.MethodeLine line) {
        var t = line.tranches();
        return new MethodeLineResponse(
                line.methode(),
                line.methode().getCategorie(),
                line.methode().getSousCategorie(),
                t.getOrDefault(AgeBracketEnum.BR_10_14, 0L),
                t.getOrDefault(AgeBracketEnum.BR_15_19, 0L),
                t.getOrDefault(AgeBracketEnum.BR_20_24, 0L),
                t.getOrDefault(AgeBracketEnum.BR_25_49, 0L),
                t.getOrDefault(AgeBracketEnum.BR_50_PLUS, 0L),
                line.total()
        );
    }

    private static PvvihSectionResponse toResponse(java.util.Map<AgeBracketEnum, Long> brackets) {
        long t10 = brackets.getOrDefault(AgeBracketEnum.BR_10_14, 0L);
        long t15 = brackets.getOrDefault(AgeBracketEnum.BR_15_19, 0L);
        long t20 = brackets.getOrDefault(AgeBracketEnum.BR_20_24, 0L);
        long t25 = brackets.getOrDefault(AgeBracketEnum.BR_25_49, 0L);
        long t50 = brackets.getOrDefault(AgeBracketEnum.BR_50_PLUS, 0L);
        return new PvvihSectionResponse(t10, t15, t20, t25, t50, t10 + t15 + t20 + t25 + t50);
    }

    private static MatriceCellResponse toResponse(RapportData.MatriceCell cell) {
        return new MatriceCellResponse(cell.source(), cell.cible(), cell.nombre());
    }
}
