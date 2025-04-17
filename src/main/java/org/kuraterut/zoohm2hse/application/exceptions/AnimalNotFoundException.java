package org.kuraterut.zoohm2hse.application.exceptions;

public class AnimalNotFoundException extends RuntimeException {
    public AnimalNotFoundException(String message) {
        super(message);
    }
}
