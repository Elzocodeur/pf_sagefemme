package com.example.pfsagefemme.modules.rapport.application.service;

import com.example.pfsagefemme.modules.contraception.domain.model.entity.ChangementMethode;
import com.example.pfsagefemme.modules.contraception.domain.port.out.ChangementMethodeRepository;
import com.example.pfsagefemme.modules.patient.domain.port.out.PatienteRepository;
import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
import com.example.pfsagefemme.modules.postesante.domain.port.out.PosteSanteRepository;
import com.example.pfsagefemme.modules.rapport.domain.model.RapportData;
import com.example.pfsagefemme.modules.rapport.domain.model.entity.LigneMatriceChangement;
import com.example.pfsagefemme.modules.rapport.domain.model.entity.LigneRapport;
import com.example.pfsagefemme.modules.rapport.domain.model.entity.RapportMensuel;
import com.example.pfsagefemme.modules.rapport.domain.port.in.GenerateRapportMensuelUseCase;
import com.example.pfsagefemme.modules.rapport.domain.port.in.GetRapportMensuelUseCase;
import com.example.pfsagefemme.modules.rapport.domain.port.in.SubmitRapportMensuelUseCase;
import com.example.pfsagefemme.modules.rapport.domain.port.out.RapportMensuelRepository;
import com.example.pfsagefemme.modules.user.domain.exception.UserNotFoundException;
import com.example.pfsagefemme.modules.user.domain.model.entity.SageFemme;
import com.example.pfsagefemme.modules.user.domain.port.out.UtilisateurRepository;
import com.example.pfsagefemme.shared.domain.enums.AgeBracketEnum;
import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutRapportEnum;
import com.example.pfsagefemme.shared.domain.enums.TypeRapportEnum;
import com.example.pfsagefemme.shared.domain.exception.BusinessRuleException;
import com.example.pfsagefemme.shared.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Genere et gere le cycle de vie du rapport mensuel PF.
 *
 * Strategie de comptage (rapport pour le mois M de l'annee A) :
 *   - 5.2.1 Nouvelles  : patientes avec dateInscription dans [M-1er, M-fin]
 *   - Anciennes PV     : inscrites avant le mois et revues (visite validee) pendant le mois
 *
 * Le rapport est individuel : seules les patientes de la sage-femme sont comptees
 * (contrainte unique mois/annee/sage-femme). La vue poste est le rapport du chef de poste.
 *   - 5.2.2 Actives    : snapshot - patientes statutPatiente = ACTIVE actuellement
 *   - 5.2.3 PVVIH      : snapshot - PVVIH ACTIVE actuellement
 *   - 5.2.4 Inactives  : ARRETEE avec dateArret dans le mois OU ABANDON courant OU INACTIVE courant
 *   - Matrice 5.2.1    : ChangementMethode dont la date est dans le mois
 *
 * La generation est idempotente : si un rapport BROUILLON existe deja pour la periode + sage-femme,
 * il est regenere ; s'il est VALIDE/SOUMIS, regeneration refusee.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RapportMensuelService implements
        GenerateRapportMensuelUseCase,
        GetRapportMensuelUseCase,
        SubmitRapportMensuelUseCase {

    private final RapportMensuelRepository rapportRepository;
    private final PatienteRepository patienteRepository;
    private final ChangementMethodeRepository changementMethodeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PosteSanteRepository posteSanteRepository;

    // ====== Generation ======================================================

    @Override
    public RapportData generate(Command command) {
        validate(command);
        SageFemme sageFemme = resolveSageFemme(command.sageFemmeId());
        PosteSante poste = resolvePoste(command.posteSanteId(), sageFemme);

        RapportMensuel rapport = rapportRepository
                .findByMoisAndAnneeAndSageFemmeId(command.mois(), command.annee(), command.sageFemmeId())
                .map(existing -> {
                    if (existing.getStatut() != StatutRapportEnum.BROUILLON) {
                        throw new BusinessRuleException("Le rapport est deja " + existing.getStatut() + " - regeneration impossible");
                    }
                    existing.getLignes().clear();
                    existing.getMatriceChangements().clear();
                    return existing;
                })
                .orElseGet(() -> RapportMensuel.builder()
                        .mois(command.mois())
                        .annee(command.annee())
                        .posteSante(poste)
                        .sageFemme(sageFemme)
                        .statut(StatutRapportEnum.BROUILLON)
                        .dateGeneration(LocalDateTime.now())
                        .build());

        rapport.setDateGeneration(LocalDateTime.now());

        YearMonth ym = YearMonth.of(command.annee(), command.mois());
        LocalDate from = ym.atDay(1);
        LocalDate to = ym.atEndOfMonth();
        UUID posteId = poste == null ? null : poste.getId();
        // Rapport individuel : seules les patientes suivies par cette sage-femme sont comptees.
        UUID sfId = sageFemme.getId();

        // Lignes des sections 5.2.1 / 5.2.2 / 5.2.4
        addLignes(rapport, TypeRapportEnum.NOUVELLES_PROGRAMME,
                (m, b) -> patienteRepository.countNouvellesByMethodeAndAge(m, b.getMin(), b.getMax(), from, to, posteId, sfId));
        addLignes(rapport, TypeRapportEnum.ACTIVES_METHODE,
                (m, b) -> patienteRepository.countActivesByMethodeAndAge(m, b.getMin(), b.getMax(), posteId, sfId));
        addLignes(rapport, TypeRapportEnum.INACTIVES_METHODE,
                (m, b) -> patienteRepository.countInactivesByMethodeAndAge(m, b.getMin(), b.getMax(), from, to, posteId, sfId));

        // Section 5.2.3 PVVIH (champs scalaires sur l'entete)
        long pvvih10_14 = patienteRepository.countPvvihActivesByAge(10, 14, posteId, sfId);
        long pvvih15_19 = patienteRepository.countPvvihActivesByAge(15, 19, posteId, sfId);
        long pvvih20_24 = patienteRepository.countPvvihActivesByAge(20, 24, posteId, sfId);
        long pvvih25_49 = patienteRepository.countPvvihActivesByAge(25, 49, posteId, sfId);
        long pvvih50p = patienteRepository.countPvvihActivesByAge(50, 200, posteId, sfId);
        rapport.setPvvihTranche10_14((int) pvvih10_14);
        rapport.setPvvihTranche15_19((int) pvvih15_19);
        rapport.setPvvihTranche20_24((int) pvvih20_24);
        rapport.setPvvihTranche25_49((int) pvvih25_49);
        rapport.setPvvihTranche50Plus((int) pvvih50p);
        rapport.setPvvihTotal((int) (pvvih10_14 + pvvih15_19 + pvvih20_24 + pvvih25_49 + pvvih50p));

        // Matrice 5.2.1 - changements de methode
        List<ChangementMethode> changements = changementMethodeRepository
                .findByDateBetweenAndPosteSanteId(from, to, posteId);
        Map<String, Long> matrice = new java.util.HashMap<>();
        for (ChangementMethode c : changements) {
            var suiviPar = c.getPatiente() == null ? null : c.getPatiente().getSageFemme();
            if (suiviPar == null || !sfId.equals(suiviPar.getId())) continue;
            String key = c.getAncienneMethode().name() + "|" + c.getNouvelleMethode().name();
            matrice.merge(key, 1L, Long::sum);
        }
        matrice.forEach((key, nombre) -> {
            String[] parts = key.split("\\|");
            rapport.getMatriceChangements().add(LigneMatriceChangement.builder()
                    .rapportMensuel(rapport)
                    .methodeSource(MethodeContraceptiveEnum.valueOf(parts[0]))
                    .methodeCible(MethodeContraceptiveEnum.valueOf(parts[1]))
                    .nombre(nombre.intValue())
                    .build());
        });

        RapportMensuel saved = rapportRepository.save(rapport);
        return assemble(saved);
    }

    private void addLignes(RapportMensuel rapport,
                           TypeRapportEnum type,
                           java.util.function.BiFunction<MethodeContraceptiveEnum, AgeBracketEnum, Long> counter) {
        for (MethodeContraceptiveEnum methode : MethodeContraceptiveEnum.values()) {
            long t10 = counter.apply(methode, AgeBracketEnum.BR_10_14);
            long t15 = counter.apply(methode, AgeBracketEnum.BR_15_19);
            long t20 = counter.apply(methode, AgeBracketEnum.BR_20_24);
            long t25 = counter.apply(methode, AgeBracketEnum.BR_25_49);
            long t50 = counter.apply(methode, AgeBracketEnum.BR_50_PLUS);
            long total = t10 + t15 + t20 + t25 + t50;

            // On omet les lignes vides pour ne pas surcharger le rapport persiste.
            if (total == 0) continue;

            rapport.getLignes().add(LigneRapport.builder()
                    .rapportMensuel(rapport)
                    .typeRapport(type)
                    .methode(methode)
                    .tranche10_14((int) t10)
                    .tranche15_19((int) t15)
                    .tranche20_24((int) t20)
                    .tranche25_49((int) t25)
                    .tranche50Plus((int) t50)
                    .total((int) total)
                    .build());
        }
    }

    // ====== Lecture =========================================================

    @Override
    @Transactional(readOnly = true)
    public RapportData byId(UUID id) {
        RapportMensuel rapport = rapportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rapport introuvable : " + id));
        return assemble(rapport);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RapportData> byPeriode(int mois, int annee, UUID sageFemmeId) {
        return rapportRepository.findByMoisAndAnneeAndSageFemmeId(mois, annee, sageFemmeId).map(this::assemble);
    }

    private RapportData assemble(RapportMensuel rapport) {
        List<RapportData.MethodeLine> section521 = buildSection(rapport, TypeRapportEnum.NOUVELLES_PROGRAMME);
        List<RapportData.MethodeLine> section522 = buildSection(rapport, TypeRapportEnum.ACTIVES_METHODE);
        List<RapportData.MethodeLine> section524 = buildSection(rapport, TypeRapportEnum.INACTIVES_METHODE);

        Map<AgeBracketEnum, Long> section523 = new EnumMap<>(AgeBracketEnum.class);
        section523.put(AgeBracketEnum.BR_10_14, longOrZero(rapport.getPvvihTranche10_14()));
        section523.put(AgeBracketEnum.BR_15_19, longOrZero(rapport.getPvvihTranche15_19()));
        section523.put(AgeBracketEnum.BR_20_24, longOrZero(rapport.getPvvihTranche20_24()));
        section523.put(AgeBracketEnum.BR_25_49, longOrZero(rapport.getPvvihTranche25_49()));
        section523.put(AgeBracketEnum.BR_50_PLUS, longOrZero(rapport.getPvvihTranche50Plus()));

        List<RapportData.MatriceCell> matrice = rapport.getMatriceChangements().stream()
                .map(c -> new RapportData.MatriceCell(c.getMethodeSource(), c.getMethodeCible(), c.getNombre()))
                .toList();

        long nouvellesTotal = sumTotal(section521);
        long activesTotal = sumTotal(section522);
        long inactivesTotal = sumTotal(section524);
        long pvvihTotal = section523.values().stream().mapToLong(Long::longValue).sum();

        YearMonth ym = YearMonth.of(rapport.getAnnee(), rapport.getMois());
        long anciennesTotal = patienteRepository.countAnciennesPv(
                ym.atDay(1), ym.atEndOfMonth(),
                rapport.getPosteSante() == null ? null : rapport.getPosteSante().getId(),
                rapport.getSageFemme() == null ? null : rapport.getSageFemme().getId());

        RapportData.Stats stats = new RapportData.Stats(nouvellesTotal, anciennesTotal, activesTotal, inactivesTotal, pvvihTotal);

        return new RapportData(rapport, stats, section521, List.of(), section522, section523, section524, matrice);
    }

    private List<RapportData.MethodeLine> buildSection(RapportMensuel rapport, TypeRapportEnum type) {
        List<RapportData.MethodeLine> lines = new ArrayList<>();
        for (MethodeContraceptiveEnum methode : MethodeContraceptiveEnum.values()) {
            Map<AgeBracketEnum, Long> tranches = new EnumMap<>(AgeBracketEnum.class);
            long total = 0;
            var ligneOpt = rapport.getLignes().stream()
                    .filter(l -> l.getTypeRapport() == type && l.getMethode() == methode)
                    .findFirst();
            if (ligneOpt.isPresent()) {
                LigneRapport l = ligneOpt.get();
                tranches.put(AgeBracketEnum.BR_10_14, longOrZero(l.getTranche10_14()));
                tranches.put(AgeBracketEnum.BR_15_19, longOrZero(l.getTranche15_19()));
                tranches.put(AgeBracketEnum.BR_20_24, longOrZero(l.getTranche20_24()));
                tranches.put(AgeBracketEnum.BR_25_49, longOrZero(l.getTranche25_49()));
                tranches.put(AgeBracketEnum.BR_50_PLUS, longOrZero(l.getTranche50Plus()));
                total = longOrZero(l.getTotal());
            } else {
                for (AgeBracketEnum b : AgeBracketEnum.values()) tranches.put(b, 0L);
            }
            lines.add(new RapportData.MethodeLine(methode, tranches, total));
        }
        return lines;
    }

    private long sumTotal(List<RapportData.MethodeLine> lines) {
        return lines.stream().mapToLong(RapportData.MethodeLine::total).sum();
    }

    private long longOrZero(Integer i) { return i == null ? 0L : i.longValue(); }

    // ====== Submit / Validate ===============================================

    @Override
    public RapportMensuel valider(UUID rapportId) {
        return transition(rapportId, StatutRapportEnum.VALIDE);
    }

    @Override
    public RapportMensuel soumettre(UUID rapportId) {
        return transition(rapportId, StatutRapportEnum.SOUMIS);
    }

    private RapportMensuel transition(UUID rapportId, StatutRapportEnum newStatut) {
        RapportMensuel rapport = rapportRepository.findById(rapportId)
                .orElseThrow(() -> new ResourceNotFoundException("Rapport introuvable : " + rapportId));
        if (rapport.getStatut() == StatutRapportEnum.SOUMIS) {
            throw new BusinessRuleException("Le rapport est deja SOUMIS, transition impossible");
        }
        rapport.setStatut(newStatut);
        return rapportRepository.save(rapport);
    }

    // ====== Helpers =========================================================

    private void validate(Command command) {
        if (command.mois() < 1 || command.mois() > 12) {
            throw new BusinessRuleException("Mois invalide : " + command.mois());
        }
        if (command.annee() < 2000 || command.annee() > 2100) {
            throw new BusinessRuleException("Annee invalide : " + command.annee());
        }
    }

    private SageFemme resolveSageFemme(UUID id) {
        var u = utilisateurRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (!(u instanceof SageFemme sf)) {
            throw new BusinessRuleException("Seule une sage-femme peut generer un rapport");
        }
        return sf;
    }

    private PosteSante resolvePoste(UUID posteSanteId, SageFemme sageFemme) {
        if (posteSanteId != null) {
            return posteSanteRepository.findById(posteSanteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Poste introuvable : " + posteSanteId));
        }
        return sageFemme.getPosteSante();
    }
}
