package com.example.pfsagefemme.modules.user.domain.port.in.command;

import java.util.UUID;

public record ChangePasswordCommand(UUID userId, String currentPassword, String newPassword) {}
