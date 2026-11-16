package com.example.pfsagefemme.modules.patient.infrastructure.rest.dto;

import com.example.pfsagefemme.shared.domain.enums.EthnieEnum;
import com.example.pfsagefemme.shared.domain.enums.GenreDeVieEnum;
import com.example.pfsagefemme.shared.domain.enums.NiveauInstructionEnum;
import com.example.pfsagefemme.shared.domain.enums.ProfessionMariEnum;
import com.example.pfsagefemme.shared.domain.enums.ReligionEnum;
import com.example.pfsagefemme.shared.domain.enums.SituationMatrimonialeEnum;
import com.example.pfsagefemme.shared.domain.enums.SourceInformationEnum;
import com.example.pfsagefemme.shared.domain.enums.StadeVihOmsEnum;
import com.example.pfsagefemme.shared.domain.enums.StatutPatienteEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Schema(description = "Patiente - vue complete")
public record PatienteResponse(
        UUID id,
        String numeroDossier,
        String prenom,
        String nom,
        Integer age,
        LocalDate dateNaissance,
        String telephone,
        String profession,
        String adresse,
        EthnieEnum ethnie,
        ReligionEnum religion,
        SituationMatrimonialeEnum situationMatrimoniale,
        NiveauInstructionEnum niveauInstruction,
        Set<GenreDeVieEnum> genreDeVie,
        ProfessionMariEnum professionMari,
        SourceInformationEnum sourceInformation,
        Boolean referee,
        String origineReference,
        Boolean pvvih,
        StadeVihOmsEnum stadeOms,
        Boolean sousArv,
        String traitementArv,
        StatutPatienteEnum statutPatiente,
        String motifArret,
        LocalDate dateArret,
        LocalDate dateInscription,
        UUID posteSanteId,
        String posteSanteNom,
        UUID sageFemmeId
) {}
