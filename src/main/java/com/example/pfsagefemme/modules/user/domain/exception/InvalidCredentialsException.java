package com.example.pfsagefemme.modules.user.domain.exception;

import com.example.pfsagefemme.shared.domain.exception.DomainException;

public class InvalidCredentialsException extends DomainException {
    public InvalidCredentialsException() {
        super("Identifiants invalides");
    }
}
