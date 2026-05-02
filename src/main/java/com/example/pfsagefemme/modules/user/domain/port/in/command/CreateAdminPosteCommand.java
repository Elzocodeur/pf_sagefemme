package com.example.pfsagefemme.modules.user.domain.port.in.command;

import java.util.UUID;

/** Commande de creation d'un AdminPoste - issue d'une action SUPER_ADMIN. */
public record CreateAdminPosteCommand(
        String nom,
        String prenom,
        String email,
        String username,
        String motDePasse,
        String telephone,
        UUID posteSanteId
) {}
