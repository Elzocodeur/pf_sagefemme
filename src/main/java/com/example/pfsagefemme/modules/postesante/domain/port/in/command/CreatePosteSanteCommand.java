package com.example.pfsagefemme.modules.postesante.domain.port.in.command;

public record CreatePosteSanteCommand(
        String nom,
        String adresse,
        String telephone,
        String district,
        String region,
        String responsableNom,
        String responsableTitre,
        String responsableEmail,
        String responsableTelephone,
        Boolean actif
) {}
