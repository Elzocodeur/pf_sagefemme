package com.example.pfsagefemme.modules.user.domain.port.in;

import com.example.pfsagefemme.modules.user.domain.model.entity.SuperAdmin;
import com.example.pfsagefemme.modules.user.domain.port.in.command.CreateSuperAdminCommand;

public interface CreateSuperAdminUseCase {
    SuperAdmin create(CreateSuperAdminCommand command);
}
