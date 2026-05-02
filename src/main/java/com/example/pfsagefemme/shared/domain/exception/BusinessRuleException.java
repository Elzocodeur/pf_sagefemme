package com.example.pfsagefemme.shared.domain.exception;

/**
 * Levee quand une regle metier est violee
 * (ex: prescrire une methode contraceptive incompatible avec une grossesse).
 */
public class BusinessRuleException extends DomainException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
