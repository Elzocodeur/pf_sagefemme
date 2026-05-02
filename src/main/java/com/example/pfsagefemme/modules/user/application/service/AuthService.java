package com.example.pfsagefemme.modules.user.application.service;

import com.example.pfsagefemme.modules.user.domain.exception.InvalidCredentialsException;
import com.example.pfsagefemme.modules.user.domain.exception.UserNotFoundException;
import com.example.pfsagefemme.modules.user.domain.model.entity.Utilisateur;
import com.example.pfsagefemme.modules.user.domain.port.in.AuthenticateUseCase;
import com.example.pfsagefemme.modules.user.domain.port.in.ChangePasswordUseCase;
import com.example.pfsagefemme.modules.user.domain.port.in.GetCurrentUserUseCase;
import com.example.pfsagefemme.modules.user.domain.port.in.command.ChangePasswordCommand;
import com.example.pfsagefemme.modules.user.domain.port.in.command.LoginCommand;
import com.example.pfsagefemme.modules.user.domain.port.out.PasswordEncoderPort;
import com.example.pfsagefemme.modules.user.domain.port.out.UtilisateurRepository;
import com.example.pfsagefemme.shared.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use cases d'authentification : login, lecture du profil courant, changement de mot de passe.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService implements AuthenticateUseCase, GetCurrentUserUseCase, ChangePasswordUseCase {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional(readOnly = true)
    public AuthResult authenticate(LoginCommand command) {
        Utilisateur user = utilisateurRepository.findByEmail(command.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!Boolean.TRUE.equals(user.getActif())) {
            throw new InvalidCredentialsException();
        }
        if (!passwordEncoder.matches(command.motDePasse(), user.getMotDePasse())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generate(user.getId(), user.getEmail(), user.getRole());
        return new AuthResult(user, token, jwtService.expirationSeconds());
    }

    @Override
    @Transactional(readOnly = true)
    public Utilisateur byId(UUID id) {
        return utilisateurRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public void changePassword(ChangePasswordCommand command) {
        Utilisateur user = utilisateurRepository.findById(command.userId())
                .orElseThrow(() -> new UserNotFoundException(command.userId()));
        if (!passwordEncoder.matches(command.currentPassword(), user.getMotDePasse())) {
            throw new InvalidCredentialsException();
        }
        user.setMotDePasse(passwordEncoder.hash(command.newPassword()));
        utilisateurRepository.save(user);
    }
}
