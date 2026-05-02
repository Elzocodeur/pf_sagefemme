package com.example.pfsagefemme.modules.user.domain.port.in.command;

public record CreateSuperAdminCommand(
        String nom,
        String prenom,
        String email,
        String username,
        String motDePasse,
        String telephone,
        String niveauAcces
) {}
