package com.example.pfsagefemme.shared.domain.exception;

/**
 * Levee quand un agregat reference n'existe pas.
 * Les sous-classes par module (ex: PatientNotFoundException) heritent de celle-ci.
 */
public class ResourceNotFoundException extends DomainException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
