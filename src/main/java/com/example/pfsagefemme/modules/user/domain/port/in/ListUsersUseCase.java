package com.example.pfsagefemme.modules.user.domain.port.in;

import com.example.pfsagefemme.modules.user.domain.model.entity.Utilisateur;
import com.example.pfsagefemme.shared.domain.enums.RoleEnum;

import java.util.List;

public interface ListUsersUseCase {
    /** @param role facultatif (null = tous les roles) */
    List<Utilisateur> list(RoleEnum role);
}
