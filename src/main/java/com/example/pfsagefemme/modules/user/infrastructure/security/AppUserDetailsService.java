package com.example.pfsagefemme.modules.user.infrastructure.security;

import com.example.pfsagefemme.modules.user.domain.model.entity.Utilisateur;
import com.example.pfsagefemme.modules.user.domain.port.out.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Charge un utilisateur pour Spring Security.
 *
 * L'identifiant accepte par {@link #loadByUsername(String)} peut etre :
 *   - un UUID (utilise par le filtre JWT, subject du token)
 *   - un email
 *   - un username court (ex: "chef_cs1")
 */
@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        return tryLoadByUuid(identifier)
                .or(() -> utilisateurRepository.findByEmailOrUsername(identifier))
                .map(AppUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + identifier));
    }

    private Optional<Utilisateur> tryLoadByUuid(String s) {
        try {
            return utilisateurRepository.findById(UUID.fromString(s));
        } catch (IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }
}
