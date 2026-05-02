package com.example.pfsagefemme.modules.user.domain.port.in;

import java.util.UUID;

public interface DeactivateUserUseCase {
    void deactivate(UUID userId);
    void activate(UUID userId);
}
