package com.example.pfsagefemme.modules.user.domain.port.in;

import com.example.pfsagefemme.modules.user.domain.model.entity.AdminPoste;
import com.example.pfsagefemme.modules.user.domain.port.in.command.CreateAdminPosteCommand;

public interface CreateAdminPosteUseCase {
    AdminPoste create(CreateAdminPosteCommand command);
}
