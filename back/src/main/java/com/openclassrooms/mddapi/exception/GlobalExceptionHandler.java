package com.openclassrooms.mddapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/**
 * Gestionnaire global d'exceptions pour l'API REST.
 * Capture les exceptions spécifiques et les transforme en réponses HTTP appropriées.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gère les exceptions ResourceNotFoundException.
     * Renvoie une réponse 404 Not Found avec les détails de l'erreur.
     * 
     * @param ex L'exception capturée
     * @param request La requête web qui a généré l'exception
     * @return ResponseEntity contenant les détails de l'erreur
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse errorDetails = new ErrorResponse(new Date(), HttpStatus.NOT_FOUND.value(),
                "Not Found", ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Gère les exceptions ResourceAlreadyExistsException.
     * Renvoie une réponse 409 Conflict avec les détails de l'erreur.
     * 
     * @param ex L'exception capturée
     * @param request La requête web qui a généré l'exception
     * @return ResponseEntity contenant les détails de l'erreur
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<?> resourceAlreadyExistsException(ResourceAlreadyExistsException ex, WebRequest request) {
        ErrorResponse errorDetails = new ErrorResponse(new Date(), HttpStatus.CONFLICT.value(),
                "Conflict", ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    /**
     * Gère les exceptions UnauthorizedException.
     * Renvoie une réponse 403 Forbidden avec les détails de l'erreur.
     * 
     * @param ex L'exception capturée
     * @param request La requête web qui a généré l'exception
     * @return ResponseEntity contenant les détails de l'erreur
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> unauthorizedException(UnauthorizedException ex, WebRequest request) {
        ErrorResponse errorDetails = new ErrorResponse(new Date(), HttpStatus.FORBIDDEN.value(),
                "Forbidden", ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    /**
     * Gère toutes les autres exceptions non capturées.
     * Renvoie une réponse 500 Internal Server Error avec les détails de l'erreur.
     * 
     * @param ex L'exception capturée
     * @param request La requête web qui a généré l'exception
     * @return ResponseEntity contenant les détails de l'erreur
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorResponse errorDetails = new ErrorResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error", ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

/**
 * Classe représentant la structure de réponse d'erreur uniforme pour l'API.
 */
class ErrorResponse {
    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String details;

    public ErrorResponse(Date timestamp, int status, String error, String message, String details) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
    }

    // Getters and setters
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}