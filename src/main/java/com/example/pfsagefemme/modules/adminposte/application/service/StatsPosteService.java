package com.example.pfsagefemme.modules.adminposte.application.service;

import com.example.pfsagefemme.modules.adminposte.domain.port.in.GetStatsPosteUseCase;
import com.example.pfsagefemme.modules.consultation.domain.model.entity.FicheConsultation;
import com.example.pfsagefemme.modules.consultation.domain.model.entity.Visite;
import com.example.pfsagefemme.modules.consultation.domain.port.out.FicheConsultationRepository;
import com.example.pfsagefemme.modules.consultation.domain.port.out.VisiteRepository;
import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.modules.patient.domain.port.out.PatienteRepository;
import com.example.pfsagefemme.modules.postesante.domain.port.out.PosteSanteRepository;
import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import com.example.pfsagefemme.shared.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsPosteService implements GetStatsPosteUseCase {

    private static final java.time.format.DateTimeFormatter FR_DATE =
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final int ABANDON_THRESHOLD = PatienteRepository.ABANDON_THRESHOLD_DAYS;
    private static final int ACTIVES_RECENTES_LIMIT = 5;

    private final PatienteRepository patienteRepository;
    private final FicheConsultationRepository ficheRepository;
    private final VisiteRepository visiteRepository;
    private final PosteSanteRepository posteSanteRepository;

    @Override
    public StatsPoste load(UUID posteSanteId) {
        posteSanteRepository.findById(posteSanteId)
                .orElseThrow(() -> new ResourceNotFoundException("Poste introuvable : " + posteSanteId));

        long total = patienteRepository.countAll(posteSanteId);
        long actives = patienteRepository.countByStatut(StatutPatienteEnum.ACTIVE, posteSanteId);
        long arretees = patienteRepository.countByStatut(StatutPatienteEnum.ARRETEE, posteSanteId);
        long abandons = patienteRepository.findInactivesSinceThreshold(
                LocalDate.now().minusDays(ABANDON_THRESHOLD), posteSanteId).size();
        long pvvih = patienteRepository.countPvvih(posteSanteId);

        // Delta mensuel nouvelles PV
        YearMonth ym = YearMonth.now();
        long nowMonth = patienteRepository.countNouvellesInscritesEntre(ym.atDay(1), ym.atEndOfMonth(), posteSanteId);
        // Meme periode du mois precedent (1er -> meme jour) pour ne pas penaliser le debut de mois
        LocalDate prevFrom = ym.minusMonths(1).atDay(1);
        LocalDate prevSameDay = prevFrom.plusDays(LocalDate.now().getDayOfMonth() - 1L);
        LocalDate prevTo = prevSameDay.isAfter(ym.minusMonths(1).atEndOfMonth()) ? ym.minusMonths(1).atEndOfMonth() : prevSameDay;
        long prevMonth = patienteRepository.countNouvellesInscritesEntre(prevFrom, prevTo, posteSanteId);
        double evolution = prevMonth == 0 ? 0.0 : ((double) (nowMonth - prevMonth) / prevMonth) * 100.0;

        // Taux de retention
        long tauxRetention = total == 0 ? 0L : Math.round((double) actives / total * 100.0);

        // Actives recentes avec methode
        List<ActiveRecentePoste> activesRecentes = buildActivesRecentes(posteSanteId);

        // A surveiller: abandons + arretees recentes
        List<ASurveillerItem> aSurveiller = buildASurveiller(posteSanteId);

        // Repartition par methode (categories principales)
        List<MethodeCount> repartition = buildRepartition(posteSanteId);

        // Methode la plus sollicitee
        String methodePlusSollicitee = repartition.stream()
                .max(Comparator.comparingLong(MethodeCount::count))
                .map(MethodeCount::categorie)
                .orElse("N/A");

        return new StatsPoste(
                new KpiCard(total, "Total patientes", "Toutes patientes"),
                new KpiCard(actives, "Actives", "En suivi"),
                new KpiCard(arretees, "Arrêtées", "Arrêt volontaire"),
                new KpiCard(abandons, "Abandons", "Perdues de vue"),
                new KpiCard(pvvih, "PVVIH", "Séropositives"),
                activesRecentes,
                aSurveiller,
                repartition,
                tauxRetention,
                methodePlusSollicitee,
                evolution
        );
    }

    private List<ActiveRecentePoste> buildActivesRecentes(UUID posteSanteId) {
        return patienteRepository.findActivesOrderByLastVisitDesc(posteSanteId, ACTIVES_RECENTES_LIMIT)
                .stream()
                .map(p -> {
                    FicheConsultation fiche = ficheRepository.findByPatienteId(p.getId()).orElse(null);
                    MethodeContraceptiveEnum methode = fiche != null ? fiche.getMethodeChoisie() : null;
                    String label = methode != null
                            ? methode.getCategorie() + (methode.getSousCategorie().isBlank() ? "" : " " + methode.getSousCategorie())
                            : "Non renseignée";
                    return new ActiveRecentePoste(
                            p.getId(), p.getNumeroDossier(), p.getPrenom(), p.getNom(),
                            methode, label, p.getStatutPatiente()
                    );
                })
                .toList();
    }

    private List<ASurveillerItem> buildASurveiller(UUID posteSanteId) {
        List<Patiente> abandons = patienteRepository.findInactivesSinceThreshold(
                LocalDate.now().minusDays(ABANDON_THRESHOLD), posteSanteId);
        List<Patiente> arretees = patienteRepository.findByStatutPatienteIn(
                List.of(StatutPatienteEnum.ARRETEE), posteSanteId);

        List<ASurveillerItem> items = new java.util.ArrayList<>();

        abandons.stream().limit(3).forEach(p -> {
            Visite last = visiteRepository.findLatestByPatienteId(p.getId()).orElse(null);
            String message;
            if (last != null) {
                message = "Dernière visite le " + FR_DATE.format(last.getDate())
                        + " (il y a " + ChronoUnit.DAYS.between(last.getDate(), LocalDate.now()) + " j)";
            } else if (p.getDateInscription() != null) {
                message = "Aucune visite depuis l'inscription du " + FR_DATE.format(p.getDateInscription());
            } else {
                message = "Aucune visite enregistrée";
            }
            items.add(new ASurveillerItem("ABANDON", p.getId(), p.getPrenom(), p.getNom(), message));
        });

        arretees.stream().limit(3).forEach(p ->
                items.add(new ASurveillerItem("ARRETEE", p.getId(), p.getPrenom(), p.getNom(),
                        p.getDateArret() != null ? "Arrêt le " + FR_DATE.format(p.getDateArret()) : "Arrêt enregistré"))
        );

        return items;
    }

    private List<MethodeCount> buildRepartition(UUID posteSanteId) {
        // Regroupe les methodes par categorie
        Map<String, Long> byCategorie = Arrays.stream(MethodeContraceptiveEnum.values())
                .collect(Collectors.groupingBy(
                        MethodeContraceptiveEnum::getCategorie,
                        Collectors.summingLong(m ->
                                patienteRepository.countActivesByMethodeAndAge(m, 0, 200, posteSanteId))
                ));

        return byCategorie.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(e -> new MethodeCount(null, e.getKey(), e.getValue()))
                .toList();
    }
}
