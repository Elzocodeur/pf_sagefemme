package com.example.pfsagefemme.modules.consultation.infrastructure.rest.dto;

import com.example.pfsagefemme.shared.domain.enums.MethodeContraceptiveEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutVisiteEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Donnees d'une nouvelle visite de suivi.
 *
 * Si {@link MethodChange} est present, la visite declenche aussi un changement de methode
 * contraceptive (creation d'un ChangementMethode + maj de la fiche de consultation).
 */
@Schema(description = "Creation d'une visite de suivi")
public record CreateVisiteRequest(

        @Schema(example = "2024-10-24", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull @PastOrPresent
        LocalDate date,

        @Schema(description = "Date des dernieres regles", example = "2024-10-12")
        @PastOrPresent
        LocalDate dateDernieresRegles,

        @Schema(example = "65.4")
        @Positive
        Double poids,

        @Schema(description = "Tension arterielle au format systolique/diastolique", example = "12/8")
        @Pattern(regexp = "^\\d{1,2}/\\d{1,2}$", message = "Format attendu : systolique/diastolique (ex: 12/8)")
        @Size(max = 30)
        String tensionArterielle,

        @Schema(description = "Symptomes, tolerance, discussions, conduite a tenir")
        String observations,

        @Schema(description = "Quantite de produit remis (ex: 3 plaquettes)")
        @Size(max = 100)
        String unitesFournies,

        @Schema(description = "Date du prochain rendez-vous")
        LocalDate dateProchainRdv,

        @Schema(description = "Statut de la visite", defaultValue = "VALIDEE")
        StatutVisiteEnum statut,

        @Valid
        @Schema(description = "A renseigner uniquement si la visite acte un changement de methode")
        MethodChange methodChange
) {

    @Schema(description = "Sous-objet de changement de methode contraceptive")
    public record MethodChange(
            @NotNull MethodeContraceptiveEnum nouvelleMethode,
            @Size(max = 500) String motif
    ) {}
}
