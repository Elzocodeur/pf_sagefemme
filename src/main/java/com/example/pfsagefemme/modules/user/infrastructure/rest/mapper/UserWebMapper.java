package com.example.pfsagefemme.modules.user.infrastructure.rest.mapper;

import com.example.pfsagefemme.modules.user.domain.model.entity.AdminPoste;
import com.example.pfsagefemme.modules.user.domain.model.entity.SageFemme;
import com.example.pfsagefemme.modules.user.domain.model.entity.Utilisateur;
import com.example.pfsagefemme.modules.user.domain.port.in.GetUserStatsUseCase;
import com.example.pfsagefemme.modules.user.domain.port.in.command.ChangePasswordCommand;
import com.example.pfsagefemme.modules.user.domain.port.in.command.CreateAdminPosteCommand;
import com.example.pfsagefemme.modules.user.domain.port.in.command.CreateSageFemmeCommand;
import com.example.pfsagefemme.modules.user.domain.port.in.command.CreateSuperAdminCommand;
import com.example.pfsagefemme.modules.user.domain.port.in.command.LoginCommand;
import com.example.pfsagefemme.modules.user.infrastructure.rest.dto.ChangePasswordRequest;
import com.example.pfsagefemme.modules.user.infrastructure.rest.dto.CreateAdminPosteRequest;
import com.example.pfsagefemme.modules.user.infrastructure.rest.dto.CreateSageFemmeRequest;
import com.example.pfsagefemme.modules.user.infrastructure.rest.dto.CreateSuperAdminRequest;
import com.example.pfsagefemme.modules.user.infrastructure.rest.dto.LoginRequest;
import com.example.pfsagefemme.modules.user.infrastructure.rest.dto.UserResponse;
import com.example.pfsagefemme.modules.user.infrastructure.rest.dto.UserStatsResponse;

import java.util.UUID;

public final class UserWebMapper {

    private UserWebMapper() {}

    public static LoginCommand toCommand(LoginRequest req) {
        return new LoginCommand(req.email(), req.motDePasse());
    }

    public static CreateSuperAdminCommand toCommand(CreateSuperAdminRequest req) {
        return new CreateSuperAdminCommand(req.nom(), req.prenom(), req.email(),
                req.username(), req.motDePasse(), req.telephone(), req.niveauAcces());
    }

    public static CreateAdminPosteCommand toCommand(CreateAdminPosteRequest req) {
        return new CreateAdminPosteCommand(req.nom(), req.prenom(), req.email(),
                req.username(), req.motDePasse(), req.telephone(), req.posteSanteId());
    }

    public static CreateSageFemmeCommand toCommand(CreateSageFemmeRequest req) {
        return new CreateSageFemmeCommand(req.nom(), req.prenom(), req.email(),
                req.username(), req.motDePasse(), req.telephone(), req.matricule(), req.specialite(),
                req.posteSanteId());
    }

    public static ChangePasswordCommand toCommand(UUID userId, ChangePasswordRequest req) {
        return new ChangePasswordCommand(userId, req.currentPassword(), req.newPassword());
    }

    public static UserResponse toResponse(Utilisateur u) {
        UUID posteSanteId = null;
        String posteSanteNom = null;
        String matricule = null;
        if (u instanceof AdminPoste a && a.getPosteSante() != null) {
            posteSanteId = a.getPosteSante().getId();
            posteSanteNom = a.getPosteSante().getNom();
        } else if (u instanceof SageFemme sf) {
            if (sf.getPosteSante() != null) {
                posteSanteId = sf.getPosteSante().getId();
                posteSanteNom = sf.getPosteSante().getNom();
            }
            matricule = sf.getMatricule();
        }
        return new UserResponse(
                u.getId(), u.getNom(), u.getPrenom(), u.getEmail(), u.getUsername(),
                u.getTelephone(), u.getRole(), u.getActif(),
                posteSanteId, posteSanteNom, matricule
        );
    }

    public static UserStatsResponse toResponse(GetUserStatsUseCase.UserStats s) {
        return new UserStatsResponse(s.total(), s.superAdmins(), s.chefsDePoste(), s.sagesFemmes());
    }
}
