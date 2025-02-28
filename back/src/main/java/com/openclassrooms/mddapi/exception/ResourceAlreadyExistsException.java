package com.openclassrooms.mddapi.exception;

/**
 * Exception lancée lorsqu'on tente de créer une ressource qui existe déjà.
 * Utilisée pour indiquer les violations d'unicité (ex: nom d'utilisateur, email, nom de sujet).
 */
public class ResourceAlreadyExistsException extends RuntimeException {
    
    /**
     * Crée une nouvelle exception avec le message spécifié.
     * 
     * @param message Description détaillée de l'erreur
     */
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}