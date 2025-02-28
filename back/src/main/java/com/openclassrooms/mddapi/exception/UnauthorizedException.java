package com.openclassrooms.mddapi.exception;

/**
 * Exception lancée lorsqu'un utilisateur tente d'accéder à une ressource sans les autorisations nécessaires.
 * Cette exception est différente de l'authentification échouée et indique plutôt un problème d'autorisation
 * pour un utilisateur déjà authentifié.
 */
public class UnauthorizedException extends RuntimeException {
    
    /**
     * Crée une nouvelle exception avec le message spécifié.
     * 
     * @param message Description détaillée de l'erreur d'autorisation
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}