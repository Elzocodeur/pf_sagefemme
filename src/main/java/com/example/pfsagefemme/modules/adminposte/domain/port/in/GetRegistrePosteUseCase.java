package com.example.pfsagefemme.modules.adminposte.domain.port.in;

import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/** Ecran "Fiches PF du poste / Registre complet" du chef de poste. */
public interface GetRegistrePosteUseCase {

    record PatienteRegistre(
            UUID id,
            String numeroDossier,
            String prenom,
            String nom,
            Integer age,
            String sageFemmeNomComplet,
            MethodeContraceptiveEnum methode,
            String methodeLabel,
            StatutPatienteEnum statut,
            boolean pvvih,
            LocalDate prochainRdv
    ) {}

    record RegistrePoste(
            String posteNom,
            long totalPatientesActives,
            List<PatienteRegistre> patientes,
            long avecRetardSuivi,
            long actionRequise
    ) {}

    RegistrePoste load(UUID posteSanteId, MethodeContraceptiveEnum methodeFilter);
}
