package com.example.pfsagefemme.modules.rapport.domain.port.in;

import com.example.pfsagefemme.modules.rapport.domain.model.RapportNationalData;

public interface GetRapportNationalUseCase {
    RapportNationalData load(int mois, int annee);
}
