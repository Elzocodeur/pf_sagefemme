package com.example.pfsagefemme.modules.user.domain.port.in;

import com.example.pfsagefemme.modules.user.domain.model.entity.Utilisateur;
import com.example.pfsagefemme.modules.user.domain.port.in.command.LoginCommand;

public interface AuthenticateUseCase {

    /** Resultat de l'authentification : utilisateur + JWT signe + duree de vie en secondes. */
    record AuthResult(Utilisateur utilisateur, String token, long expiresInSeconds) {}

    AuthResult authenticate(LoginCommand command);
}
