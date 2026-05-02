package com.example.pfsagefemme.modules.user.domain.port.in;

public interface GetUserStatsUseCase {
    /** Compteurs des 4 cards de l'ecran "Utilisateurs". */
    record UserStats(long total, long superAdmins, long chefsDePoste, long sagesFemmes) {}

    UserStats stats();
}
