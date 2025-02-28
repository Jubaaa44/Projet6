package com.openclassrooms.mddapi.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.model.User;
import java.util.List;

/**
 * Service gérant les opérations métier liées aux utilisateurs.
 * Cette classe implémente les fonctionnalités CRUD pour les utilisateurs
 * et vérifie les contraintes d'unicité lors de la création ou mise à jour.
 */
@Service
public interface UserService {

    /**
     * Crée un nouvel utilisateur.
     * Vérifie que l'email et le nom d'utilisateur sont uniques avant création.
     * 
     * @param user L'entité User à créer
     * @return L'utilisateur créé, incluant l'ID généré
     * @throws RuntimeException Si l'email ou le nom d'utilisateur existe déjà
     */
    public User createUser(User user);
    
    /**
     * Récupère un utilisateur par son identifiant.
     * 
     * @param id Identifiant de l'utilisateur
     * @return L'utilisateur correspondant
     * @throws RuntimeException Si l'utilisateur n'existe pas
     */
    public User getUserById(Long id);
    /**
     * Met à jour un utilisateur existant.
     * 
     * @param id Identifiant de l'utilisateur à mettre à jour
     * @param userDetails Nouvelles données de l'utilisateur
     * @return L'utilisateur mis à jour
     * @throws RuntimeException Si l'utilisateur n'existe pas
     */
    public User updateUser(Long id, User userDetails);
    
    /**
     * Récupère tous les utilisateurs du système.
     * 
     * @return Liste de tous les utilisateurs
     */
    public List<User> getAllUsers();
    
    /**
     * Supprime un utilisateur spécifique.
     * 
     * @param id Identifiant de l'utilisateur à supprimer
     */
    public void deleteUser(Long id);
}