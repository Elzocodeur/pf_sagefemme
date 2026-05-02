package com.example.pfsagefemme.modules.user.domain.exception;

import com.example.pfsagefemme.shared.domain.exception.ResourceNotFoundException;

import java.util.UUID;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(UUID id) {
        super("Utilisateur introuvable : " + id);
    }

    public UserNotFoundException(String email) {
        super("Utilisateur introuvable : " + email);
    }
}
