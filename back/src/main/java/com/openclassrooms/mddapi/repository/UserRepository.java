package com.openclassrooms.mddapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.openclassrooms.mddapi.model.User;

/**
 * Repository pour gérer les opérations de persistance des utilisateurs.
 * Fournit les opérations CRUD standards ainsi que des méthodes personnalisées pour rechercher des utilisateurs.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Trouve un utilisateur par son adresse email.
     * Retourne un Optional qui peut être vide si aucun utilisateur n'est trouvé avec cet email.
     * 
     * @param email Adresse email de l'utilisateur
     * @return Un Optional contenant l'utilisateur s'il existe, sinon un Optional vide
     */
	Optional<User> findByEmail(String email);
    
    /**
     * Trouve un utilisateur par son nom d'utilisateur.
     * 
     * @param username Nom d'utilisateur à rechercher
     * @return L'utilisateur correspondant au nom d'utilisateur spécifié
     */
    User findByUsername(String username);
    
    /**
     * Vérifie si un utilisateur avec l'adresse email spécifiée existe déjà.
     * Utile pour la validation lors de l'inscription d'un nouvel utilisateur.
     * 
     * @param email Adresse email à vérifier
     * @return true si un utilisateur avec cet email existe, false sinon
     */
    boolean existsByEmail(String email);
    
    /**
     * Vérifie si un utilisateur avec le nom d'utilisateur spécifié existe déjà.
     * Utile pour la validation lors de l'inscription d'un nouvel utilisateur.
     * 
     * @param username Nom d'utilisateur à vérifier
     * @return true si un utilisateur avec ce nom d'utilisateur existe, false sinon
     */
    boolean existsByUsername(String username);
}