package com.example.pfsagefemme.modules.adminposte.infrastructure.rest.mapper;

import com.example.pfsagefemme.modules.adminposte.domain.port.in.GetEquipePosteUseCase;
import com.example.pfsagefemme.modules.adminposte.domain.port.in.GetRegistrePosteUseCase;
import com.example.pfsagefemme.modules.adminposte.domain.port.in.GetRapportPosteUseCase;
import com.example.pfsagefemme.modules.adminposte.domain.port.in.GetStatsPosteUseCase;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto.ASurveillerResponse;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto.ActiveRecentePosteResponse;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto.EquipePosteResponse;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto.KpiCardResponse;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto.KpiRapportResponse;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto.MethodeCountResponse;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto.MethodeLinePosteResponse;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto.MonPosteResponse;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto.PatienteRegistreResponse;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto.RapportPosteResponse;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto.RegistrePosteResponse;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto.SageFemmeEquipeResponse;
import com.example.pfsagefemme.modules.adminposte.infrastructure.rest.dto.StatsPosteResponse;
import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;

public final class ChefPosteWebMapper {

    private ChefPosteWebMapper() {}

    // ====== Equipe ============================================================

    public static EquipePosteResponse toResponse(GetEquipePosteUseCase.EquipePoste d) {
        return new EquipePosteResponse(
                d.posteNom(),
                d.totalSagesFemmes(),
                d.sagesFemmes().stream().map(ChefPosteWebMapper::toResponse).toList(),
                d.moyennePatientesParSf(),
                d.dernierRapportDate(),
                d.conformite()
        );
    }

    private static SageFemmeEquipeResponse toResponse(GetEquipePosteUseCase.SageFemmeWithStats sf) {
        return new SageFemmeEquipeResponse(
                sf.id(), sf.matricule(), sf.prenom(), sf.nom(),
                sf.username(), sf.actif(), sf.nbPatientes(), sf.derniereActivite()
        );
    }

    // ====== Stats poste =======================================================

    public static StatsPosteResponse toResponse(GetStatsPosteUseCase.StatsPoste d) {
        return new StatsPosteResponse(
                toKpi(d.total()),
                toKpi(d.actives()),
                toKpi(d.arretees()),
                toKpi(d.abandons()),
                toKpi(d.pvvih()),
                d.activesRecentes().stream().map(ar -> new ActiveRecentePosteResponse(
                        ar.id(), ar.numeroDossier(), ar.prenom(), ar.nom(),
                        ar.methode(), ar.methodeLabel(), ar.statut()
                )).toList(),
                d.aSurveiller().stream().map(a -> new ASurveillerResponse(
                        a.type(), a.patienteId(), a.prenom(), a.nom(), a.message()
                )).toList(),
                d.repartitionParMethode().stream().map(m -> new MethodeCountResponse(
                        m.methode(), m.categorie(), m.count()
                )).toList(),
                d.tauxRetentionPercent(),
                d.methodePlusSollicitee(),
                d.evolutionMensuellePercent()
        );
    }

    private static KpiCardResponse toKpi(GetStatsPosteUseCase.KpiCard c) {
        return new KpiCardResponse(c.value(), c.label(), c.description());
    }

    // ====== Registre ==========================================================

    public static RegistrePosteResponse toResponse(GetRegistrePosteUseCase.RegistrePoste d) {
        return new RegistrePosteResponse(
                d.posteNom(),
                d.totalPatientesActives(),
                d.patientes().stream().map(p -> new PatienteRegistreResponse(
                        p.id(), p.numeroDossier(), p.prenom(), p.nom(), p.age(),
                        p.sageFemmeNomComplet(), p.methode(), p.methodeLabel(),
                        p.statut(), p.pvvih(), p.prochainRdv()
                )).toList(),
                d.avecRetardSuivi(),
                d.actionRequise()
        );
    }

    // ====== Rapport ===========================================================

    public static RapportPosteResponse toResponse(GetRapportPosteUseCase.RapportPosteData d) {
        return new RapportPosteResponse(
                d.mois(), d.annee(), d.periodeLabel(), d.posteNom(), d.district(),
                toKpiRapport(d.nouvellesPv()),
                toKpiRapport(d.anciennesPv()),
                toKpiRapport(d.actives()),
                toKpiRapport(d.inactives()),
                toKpiRapport(d.pvvih()),
                d.section521().stream().map(ChefPosteWebMapper::toMethodeLine).toList(),
                d.section522().stream().map(ChefPosteWebMapper::toMethodeLine).toList(),
                d.section524().stream().map(ChefPosteWebMapper::toMethodeLine).toList()
        );
    }

    private static KpiRapportResponse toKpiRapport(GetRapportPosteUseCase.KpiRapport k) {
        return new KpiRapportResponse(k.value(), k.label(), k.evolutionPercent());
    }

    private static MethodeLinePosteResponse toMethodeLine(GetRapportPosteUseCase.MethodeLine l) {
        return new MethodeLinePosteResponse(l.methode(), l.methodeLabel(), l.tranches(), l.total());
    }

    // ====== Mon poste =========================================================

    public static MonPosteResponse toResponse(PosteSante p) {
        return new MonPosteResponse(
                p.getId(),
                p.getIdentifiantExterne(),
                p.getNom(),
                p.getDistrict(),
                p.getRegion(),
                p.getAdresse(),
                p.getTelephone(),
                p.getResponsableNom(),
                p.getResponsableTitre(),
                p.getResponsableEmail(),
                p.getResponsableTelephone(),
                Boolean.TRUE.equals(p.getActif())
        );
    }
}
