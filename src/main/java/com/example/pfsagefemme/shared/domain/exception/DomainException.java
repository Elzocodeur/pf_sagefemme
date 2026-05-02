package com.example.pfsagefemme.shared.domain.exception;

/**
 * Racine de toutes les exceptions metier.
 * Le code applicatif et les adapters traduisent les sous-classes en reponses HTTP.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }

    protected DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
