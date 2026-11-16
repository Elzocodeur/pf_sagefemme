package com.example.pfsagefemme.modules.patient.infrastructure.persistence;

import com.example.pfsagefemme.modules.patient.domain.model.entity.Patiente;
import com.example.pfsagefemme.modules.patient.domain.port.out.PatienteRepository;
import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PatientePersistenceAdapter implements PatienteRepository {

    private final PatienteJpaRepository jpaRepository;

    @Override
    public Patiente save(Patiente patiente) {
        return jpaRepository.save(patiente);
    }

    @Override
    public Optional<Patiente> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Patiente> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<Patiente> findByPosteSanteId(UUID posteSanteId) {
        return jpaRepository.findByPosteSanteId(posteSanteId);
    }

    @Override
    public List<Patiente> findBySageFemmeId(UUID sageFemmeId) {
        return jpaRepository.findBySageFemmeId(sageFemmeId);
    }

    @Override
    public List<Patiente> findByStatutPatiente(StatutPatienteEnum statut) {
        return jpaRepository.findByStatutPatiente(statut);
    }

    @Override
    public List<Patiente> findByPvvihTrue() {
        return jpaRepository.findByPvvihTrue();
    }

    @Override
    public List<Patiente> search(String query, UUID posteSanteId) {
        return jpaRepository.search(query, posteSanteId);
    }

    @Override
    public long countByStatutPatienteAndMethodeAndTrancheAge(
            StatutPatienteEnum statut,
            MethodeContraceptiveEnum methode,
            int ageMin,
            int ageMax,
            UUID posteSanteId
    ) {
        return jpaRepository.countByStatutPatienteAndMethodeAndTrancheAge(statut, methode, ageMin, ageMax, posteSanteId);
    }

    @Override
    public long countByPvvihTrue(UUID posteSanteId) {
        return jpaRepository.countAllPvvih(posteSanteId);
    }

    @Override
    public long countByPvvihTrueAndStatutPatiente(StatutPatienteEnum statut, UUID posteSanteId) {
        return jpaRepository.countPvvihByStatut(statut, posteSanteId);
    }

    @Override
    public long countByPvvihTrueAndDateInscriptionBetween(LocalDate from, LocalDate to, UUID posteSanteId) {
        return jpaRepository.countPvvihInscritesEntre(from, to, posteSanteId);
    }

    @Override
    public List<Patiente> findPvvih(UUID posteSanteId) {
        return jpaRepository.findPvvih(posteSanteId);
    }

    @Override
    public List<Patiente> findByStatutPatienteIn(List<StatutPatienteEnum> statuts, UUID posteSanteId) {
        return jpaRepository.findByStatutPatienteIn(statuts, posteSanteId);
    }

    @Override
    public List<Patiente> findInactivesSinceThreshold(LocalDate thresholdDate, UUID posteSanteId) {
        return jpaRepository.findInactivesSince(thresholdDate, posteSanteId);
    }

    @Override
    public long countByYearOfDateInscription(int year) {
        return jpaRepository.countByYearOfDateInscription(year);
    }

    @Override
    public Optional<Patiente> findByNumeroDossier(String numeroDossier) {
        return jpaRepository.findByNumeroDossier(numeroDossier);
    }

    @Override
    public long countNouvellesByMethodeAndAge(MethodeContraceptiveEnum methode, int ageMin, int ageMax,
                                              LocalDate from, LocalDate to, UUID posteSanteId, UUID sageFemmeId) {
        return jpaRepository.countNouvellesByMethodeAndAge(methode, ageMin, ageMax, from, to, posteSanteId, sageFemmeId);
    }

    @Override
    public long countActivesByMethodeAndAge(MethodeContraceptiveEnum methode, int ageMin, int ageMax,
                                            UUID posteSanteId, UUID sageFemmeId) {
        return jpaRepository.countActivesByMethodeAndAge(methode, ageMin, ageMax, posteSanteId, sageFemmeId);
    }

    @Override
    public long countInactivesByMethodeAndAge(MethodeContraceptiveEnum methode, int ageMin, int ageMax,
                                              LocalDate from, LocalDate to, UUID posteSanteId, UUID sageFemmeId) {
        return jpaRepository.countInactivesByMethodeAndAge(methode, ageMin, ageMax, from, to,
                abandonThresholdAt(to), posteSanteId, sageFemmeId);
    }

    @Override
    public long countPvvihActivesByAge(int ageMin, int ageMax, UUID posteSanteId, UUID sageFemmeId) {
        return jpaRepository.countPvvihActivesByAge(ageMin, ageMax, posteSanteId, sageFemmeId);
    }

    @Override
    public long countAbandons(UUID posteSanteId, UUID sageFemmeId) {
        return jpaRepository.countAbandons(abandonThresholdAt(LocalDate.now()), posteSanteId, sageFemmeId);
    }

    @Override
    public long countAnciennesPv(LocalDate from, LocalDate to, UUID posteSanteId, UUID sageFemmeId) {
        return jpaRepository.countAnciennesPv(from, to, posteSanteId, sageFemmeId);
    }

    /** Seuil d'abandon vu a la date {@code at} (jamais dans le futur). */
    private static LocalDate abandonThresholdAt(LocalDate at) {
        LocalDate today = LocalDate.now();
        return (at.isAfter(today) ? today : at).minusDays(ABANDON_THRESHOLD_DAYS);
    }

    @Override
    public long countNouvellesOnDate(LocalDate date, UUID posteSanteId) {
        return jpaRepository.countNouvellesOnDate(date, posteSanteId);
    }

    @Override
    public long countArreteesOnDate(LocalDate date, UUID posteSanteId) {
        return jpaRepository.countArreteesOnDate(date, posteSanteId);
    }

    @Override
    public long countAll(UUID posteSanteId) {
        return jpaRepository.countAllByPoste(posteSanteId);
    }

    @Override
    public long countByStatut(StatutPatienteEnum statut, UUID posteSanteId) {
        return jpaRepository.countByStatutAndPoste(statut, posteSanteId);
    }

    @Override
    public long countPvvih(UUID posteSanteId) {
        return jpaRepository.countPvvihByPoste(posteSanteId);
    }

    @Override
    public long countNouvellesInscritesEntre(LocalDate from, LocalDate to, UUID posteSanteId) {
        return jpaRepository.countNouvellesInscritesEntre(from, to, posteSanteId);
    }

    @Override
    public long countArreteesEntre(LocalDate from, LocalDate to, UUID posteSanteId) {
        return jpaRepository.countArreteesEntre(from, to, posteSanteId);
    }

    @Override
    public List<Patiente> findActivesOrderByLastVisitDesc(UUID posteSanteId, int limit) {
        return jpaRepository.findActivesOrderByLastVisitDesc(
                posteSanteId, PageRequest.of(0, Math.max(limit, 1))
        );
    }

    @Override
    public Page<Patiente> searchPaged(String query, UUID posteSanteId, Pageable pageable) {
        return jpaRepository.searchPaged(query, posteSanteId, pageable);
    }

    @Override
    public Page<Patiente> findByStatutPatienteInPaged(
            List<StatutPatienteEnum> statuts, UUID posteSanteId, String q, Pageable pageable) {
        return jpaRepository.findByStatutPatienteInPaged(statuts, posteSanteId, q, pageable);
    }

    @Override
    public Page<Patiente> findInactivesSinceThresholdPaged(
            LocalDate thresholdDate, UUID posteSanteId, String q, Pageable pageable) {
        return jpaRepository.findInactivesSincePaged(thresholdDate, posteSanteId, q, pageable);
    }

    @Override
    public Page<Patiente> findPvvihPaged(UUID posteSanteId, String q, Pageable pageable) {
        return jpaRepository.findPvvihPaged(posteSanteId, q, pageable);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
