package org.kuraterut.zoohm2hse.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AnimalNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAnimalNotFound(AnimalNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EnclosureNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEnclosureNotFound(EnclosureNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(EnclosureIsNotEmptyException.class)
    public ResponseEntity<ErrorResponse> handleEnclosureIsNotEmpty(EnclosureIsNotEmptyException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeedingScheduleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFeedingScheduleNotFound(FeedingScheduleNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AnimalTypeIsNotCompatibleException.class)
    public ResponseEntity<ErrorResponse> handleIncompatibleTypes(AnimalTypeIsNotCompatibleException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAnimalNameException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAnimalName(InvalidAnimalNameException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAnimalBirthdayException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAnimalBirthday(InvalidAnimalBirthdayException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EnclosureIsFullException.class)
    public ResponseEntity<ErrorResponse> handleEnclosureFull(EnclosureIsFullException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEnclosureMaxCapacityException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEnclosureMaxCapacity(InvalidEnclosureMaxCapacityException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFeedingTimeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFeedingTime(InvalidFeedingTimeException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> buildResponse(String message, HttpStatus status) {
        ErrorResponse response = new ErrorResponse(message, status);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ErrorResponse response = new ErrorResponse(
                "Internal server error: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return ResponseEntity.internalServerError().body(response);
    }
}