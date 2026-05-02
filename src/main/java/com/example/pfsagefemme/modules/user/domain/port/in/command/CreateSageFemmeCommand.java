package com.example.pfsagefemme.modules.user.domain.port.in.command;

import java.util.UUID;

/**
 * Commande de creation d'une sage-femme.
 * Si {@code posteSanteId} est null, le service utilise le poste de l'appelant (cas ADMIN_POSTE).
 * Si non null, le SUPER_ADMIN peut affecter a n'importe quel poste.
 */
public record CreateSageFemmeCommand(
        String nom,
        String prenom,
        String email,
        String username,
        String motDePasse,
        String telephone,
        String matricule,
        String specialite,
        UUID posteSanteId
) {}
