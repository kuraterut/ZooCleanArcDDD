package org.kuraterut.zoohm2hse.application.exceptions;

public class InvalidAnimalNameException extends RuntimeException {
    public InvalidAnimalNameException(String message) {
        super(message);
    }
}
