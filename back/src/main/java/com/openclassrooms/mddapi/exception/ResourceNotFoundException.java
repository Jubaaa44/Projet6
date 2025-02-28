package com.openclassrooms.mddapi.exception;

/**
 * Exception lancée lorsqu'une ressource demandée n'est pas trouvée.
 * Utilisée pour indiquer qu'une entité recherchée n'existe pas dans la base de données.
 */
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Crée une nouvelle exception avec le message spécifié.
     * 
     * @param message Description détaillée de l'erreur, généralement contenant l'identifiant de la ressource
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}