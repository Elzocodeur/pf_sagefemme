package com.example.pfsagefemme.modules.user.application.service;

import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
import com.example.pfsagefemme.modules.postesante.domain.port.out.PosteSanteRepository;
import com.example.pfsagefemme.modules.user.domain.exception.EmailAlreadyExistsException;
import com.example.pfsagefemme.modules.user.domain.exception.UserNotFoundException;
import com.example.pfsagefemme.modules.user.domain.model.entity.AdminPoste;
import com.example.pfsagefemme.modules.user.domain.model.entity.SageFemme;
import com.example.pfsagefemme.modules.user.domain.model.entity.SuperAdmin;
import com.example.pfsagefemme.modules.user.domain.model.entity.Utilisateur;
import com.example.pfsagefemme.modules.user.domain.port.in.CreateAdminPosteUseCase;
import com.example.pfsagefemme.modules.user.domain.port.in.CreateSageFemmeUseCase;
import com.example.pfsagefemme.modules.user.domain.port.in.CreateSuperAdminUseCase;
import com.example.pfsagefemme.modules.user.domain.port.in.DeactivateUserUseCase;
import com.example.pfsagefemme.modules.user.domain.port.in.GetUserStatsUseCase;
import com.example.pfsagefemme.modules.user.domain.port.in.ListUsersUseCase;
import com.example.pfsagefemme.modules.user.domain.port.in.command.CreateAdminPosteCommand;
import com.example.pfsagefemme.modules.user.domain.port.in.command.CreateSageFemmeCommand;
import com.example.pfsagefemme.modules.user.domain.port.in.command.CreateSuperAdminCommand;
import com.example.pfsagefemme.modules.user.domain.port.out.PasswordEncoderPort;
import com.example.pfsagefemme.modules.user.domain.port.out.UtilisateurRepository;
import com.example.pfsagefemme.shared.domain.enums.RoleEnum;
import com.example.pfsagefemme.shared.domain.exception.BusinessRuleException;
import com.example.pfsagefemme.shared.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Use cases de gestion des utilisateurs (creation, activation/desactivation, listing, stats).
 *
 * Regles metier appliquees ici :
 *  - email + username uniques
 *  - mot de passe systematiquement hashe avant persistance
 *  - une sage-femme creee par ADMIN_POSTE herite du poste de l'admin appelant (cross-poste interdit)
 *  - SUPER_ADMIN peut affecter une sage-femme a n'importe quel poste si posteSanteId est fourni
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserManagementService implements
        CreateSuperAdminUseCase,
        CreateAdminPosteUseCase,
        CreateSageFemmeUseCase,
        ListUsersUseCase,
        DeactivateUserUseCase,
        GetUserStatsUseCase {

    private final UtilisateurRepository utilisateurRepository;
    private final PosteSanteRepository posteSanteRepository;
    private final PasswordEncoderPort passwordEncoder;

    // ====== Creation =========================================================

    @Override
    public SuperAdmin create(CreateSuperAdminCommand command) {
        ensureEmailAvailable(command.email());
        ensureUsernameAvailable(command.username());
        SuperAdmin admin = SuperAdmin.builder()
                .nom(command.nom())
                .prenom(command.prenom())
                .email(command.email())
                .username(command.username())
                .motDePasse(passwordEncoder.hash(command.motDePasse()))
                .telephone(command.telephone())
                .role(RoleEnum.SUPER_ADMIN)
                .actif(Boolean.TRUE)
                .niveauAcces(Optional.ofNullable(command.niveauAcces()).orElse("FULL"))
                .build();
        return utilisateurRepository.save(admin);
    }

    @Override
    public AdminPoste create(CreateAdminPosteCommand command) {
        ensureEmailAvailable(command.email());
        ensureUsernameAvailable(command.username());
        PosteSante poste = posteSanteRepository.findById(command.posteSanteId())
                .orElseThrow(() -> new ResourceNotFoundException("Poste de sante introuvable : " + command.posteSanteId()));
        AdminPoste admin = AdminPoste.builder()
                .nom(command.nom())
                .prenom(command.prenom())
                .email(command.email())
                .username(command.username())
                .motDePasse(passwordEncoder.hash(command.motDePasse()))
                .telephone(command.telephone())
                .role(RoleEnum.ADMIN_POSTE)
                .actif(Boolean.TRUE)
                .posteSante(poste)
                .build();
        return utilisateurRepository.save(admin);
    }

    @Override
    public SageFemme create(UUID callerId, CreateSageFemmeCommand command) {
        Utilisateur caller = utilisateurRepository.findById(callerId)
                .orElseThrow(() -> new UserNotFoundException(callerId));

        PosteSante poste = resolvePosteForSageFemme(caller, command.posteSanteId());

        ensureEmailAvailable(command.email());
        ensureUsernameAvailable(command.username());

        SageFemme sageFemme = SageFemme.builder()
                .nom(command.nom())
                .prenom(command.prenom())
                .email(command.email())
                .username(command.username())
                .motDePasse(passwordEncoder.hash(command.motDePasse()))
                .telephone(command.telephone())
                .matricule(command.matricule())
                .specialite(command.specialite())
                .role(RoleEnum.SAGE_FEMME)
                .actif(Boolean.TRUE)
                .posteSante(poste)
                .build();
        return utilisateurRepository.save(sageFemme);
    }

    private PosteSante resolvePosteForSageFemme(Utilisateur caller, UUID posteSanteId) {
        // SUPER_ADMIN : peut affecter a n'importe quel poste, posteSanteId obligatoire
        if (caller instanceof SuperAdmin) {
            if (posteSanteId == null) {
                throw new BusinessRuleException("Un SUPER_ADMIN doit specifier le posteSanteId pour creer une sage-femme");
            }
            return posteSanteRepository.findById(posteSanteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Poste de sante introuvable : " + posteSanteId));
        }
        // ADMIN_POSTE : herite de son poste, posteSanteId ignore
        if (caller instanceof AdminPoste adminPoste && adminPoste.getPosteSante() != null) {
            return adminPoste.getPosteSante();
        }
        throw new BusinessRuleException("Seul un SUPER_ADMIN ou un ADMIN_POSTE rattache a un poste peut creer une sage-femme");
    }

    // ====== Listing ==========================================================

    @Override
    @Transactional(readOnly = true)
    public List<Utilisateur> list(RoleEnum role) {
        return role == null ? utilisateurRepository.findAll() : utilisateurRepository.findByRole(role);
    }

    // ====== Stats ============================================================

    @Override
    @Transactional(readOnly = true)
    public UserStats stats() {
        long total = utilisateurRepository.findAll().size();
        long superAdmins = utilisateurRepository.findByRole(RoleEnum.SUPER_ADMIN).size();
        long chefsDePoste = utilisateurRepository.findByRole(RoleEnum.ADMIN_POSTE).size();
        long sagesFemmes = utilisateurRepository.findByRole(RoleEnum.SAGE_FEMME).size();
        return new UserStats(total, superAdmins, chefsDePoste, sagesFemmes);
    }

    // ====== Activation =======================================================

    @Override
    public void deactivate(UUID userId) { toggleActif(userId, false); }

    @Override
    public void activate(UUID userId) { toggleActif(userId, true); }

    private void toggleActif(UUID userId, boolean actif) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        user.setActif(actif);
        utilisateurRepository.save(user);
    }

    // ====== Helpers ==========================================================

    private void ensureEmailAvailable(String email) {
        if (email != null && utilisateurRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    private void ensureUsernameAvailable(String username) {
        if (username != null && utilisateurRepository.existsByUsername(username)) {
            throw new BusinessRuleException("Identifiant deja utilise : " + username);
        }
    }
}
