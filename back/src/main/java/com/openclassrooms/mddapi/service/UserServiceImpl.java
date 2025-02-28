package com.openclassrooms.mddapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import java.util.List;

/**
 * Service gérant les opérations métier liées aux utilisateurs.
 * Cette classe implémente les fonctionnalités CRUD pour les utilisateurs
 * et vérifie les contraintes d'unicité lors de la création ou mise à jour.
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Crée un nouvel utilisateur.
     * Vérifie que l'email et le nom d'utilisateur sont uniques avant création.
     * 
     * @param user L'entité User à créer
     * @return L'utilisateur créé, incluant l'ID généré
     * @throws RuntimeException Si l'email ou le nom d'utilisateur existe déjà
     */
    public User createUser(User user) {
        // Vérifier si email ou username existe déjà
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        return userRepository.save(user);
    }
    
    /**
     * Récupère un utilisateur par son identifiant.
     * 
     * @param id Identifiant de l'utilisateur
     * @return L'utilisateur correspondant
     * @throws RuntimeException Si l'utilisateur n'existe pas
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    /**
     * Met à jour un utilisateur existant.
     * 
     * @param id Identifiant de l'utilisateur à mettre à jour
     * @param userDetails Nouvelles données de l'utilisateur
     * @return L'utilisateur mis à jour
     * @throws RuntimeException Si l'utilisateur n'existe pas
     */
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        return userRepository.save(user);
    }
    
    /**
     * Récupère tous les utilisateurs du système.
     * 
     * @return Liste de tous les utilisateurs
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Supprime un utilisateur spécifique.
     * 
     * @param id Identifiant de l'utilisateur à supprimer
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}