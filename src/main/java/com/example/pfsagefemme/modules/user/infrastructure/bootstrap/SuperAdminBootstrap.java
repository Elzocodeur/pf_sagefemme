package com.example.pfsagefemme.modules.user.infrastructure.bootstrap;

import com.example.pfsagefemme.modules.user.domain.model.entity.SuperAdmin;
import com.example.pfsagefemme.modules.user.domain.port.out.PasswordEncoderPort;
import com.example.pfsagefemme.modules.user.domain.port.out.UtilisateurRepository;
import com.example.pfsagefemme.shared.domain.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Cree un SuperAdmin par defaut au demarrage si aucun n'existe encore.
 * Mot de passe et email lus depuis les proprietes app.bootstrap.super-admin.*
 *
 * Securite : afficher un warning evident pour rappeler de changer le mot de passe.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SuperAdminBootstrap implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoderPort passwordEncoder;

    @Value("${app.bootstrap.super-admin.email}")
    private String email;

    @Value("${app.bootstrap.super-admin.password}")
    private String password;

    @Value("${app.bootstrap.super-admin.nom}")
    private String nom;

    @Value("${app.bootstrap.super-admin.prenom}")
    private String prenom;

    @Override
    @Transactional
    public void run(String... args) {
        if (!utilisateurRepository.findByRole(RoleEnum.SUPER_ADMIN).isEmpty()) {
            return;
        }
        SuperAdmin admin = SuperAdmin.builder()
                .nom(nom)
                .prenom(prenom)
                .email(email)
                .username("admin")
                .motDePasse(passwordEncoder.hash(password))
                .role(RoleEnum.SUPER_ADMIN)
                .actif(Boolean.TRUE)
                .niveauAcces("FULL")
                .build();
        utilisateurRepository.save(admin);
        log.warn("==========================================================================");
        log.warn(" SuperAdmin par defaut cree : {}", email);
        log.warn(" Mot de passe initial : (voir BOOTSTRAP_SUPER_ADMIN_PASSWORD)");
        log.warn(" >>> CHANGEZ CE MOT DE PASSE IMMEDIATEMENT via POST /api/v1/auth/change-password");
        log.warn("==========================================================================");
    }
}
