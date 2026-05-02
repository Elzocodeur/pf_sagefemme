package com.example.pfsagefemme.modules.consultation.domain.port.in.command;

import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutVisiteEnum;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Commande de creation d'une visite de suivi.
 *
 * Si {@link MethodChange} est non null, la commande declenche EN PLUS :
 *   - la creation d'un {@code ChangementMethode} (DE l'ancienne methode VERS la nouvelle)
 *   - la mise a jour de la methode courante sur la {@code FicheConsultation} de la patiente
 *
 * Le tout dans la meme transaction que la visite.
 */
public record CreateVisiteCommand(
        UUID patienteId,
        UUID sageFemmeId,
        LocalDate date,
        LocalDate dateDernieresRegles,
        Double poids,
        String tensionArterielle,
        String observations,
        String unitesFournies,
        LocalDate dateProchainRdv,
        StatutVisiteEnum statut,
        MethodChange methodChange
) {

    /** Sous-commande optionnelle - declenche le changement de methode contraceptive. */
    public record MethodChange(
            MethodeContraceptiveEnum nouvelleMethode,
            String motif
    ) {}
}
