package com.example.pfsagefemme.modules.user.domain.port.in;

import com.example.pfsagefemme.modules.user.domain.model.entity.Utilisateur;

import java.util.UUID;

public interface GetCurrentUserUseCase {
    Utilisateur byId(UUID id);
}
