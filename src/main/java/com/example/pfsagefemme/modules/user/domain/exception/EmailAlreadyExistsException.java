package com.example.pfsagefemme.modules.user.domain.exception;

import com.example.pfsagefemme.shared.domain.exception.BusinessRuleException;

public class EmailAlreadyExistsException extends BusinessRuleException {
    public EmailAlreadyExistsException(String email) {
        super("Email deja utilise : " + email);
    }
}
