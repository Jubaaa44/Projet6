package com.openclassrooms.mddapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.model.Subject;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import java.util.List;

/**
 * Service gérant les opérations métier liées aux sujets/thématiques.
 * Cette classe implémente les fonctionnalités CRUD pour les sujets ainsi que
 * des méthodes spécifiques pour gérer les abonnements des utilisateurs aux sujets.
 */
@Service
public class SubjectService {
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Crée un nouveau sujet.
     * Vérifie que le nom du sujet est unique avant création.
     * 
     * @param subject L'entité Subject à créer
     * @return Le sujet créé, incluant l'ID généré
     * @throws RuntimeException Si un sujet avec le même nom existe déjà
     */
    public Subject createSubject(Subject subject) {
        if (subjectRepository.existsByName(subject.getName())) {
            throw new RuntimeException("Subject name already exists");
        }
        return subjectRepository.save(subject);
    }
    
    /**
     * Récupère un sujet par son identifiant.
     * 
     * @param id Identifiant du sujet
     * @return Le sujet correspondant
     * @throws RuntimeException Si le sujet n'existe pas
     */
    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Subject not found"));
    }
    
    /**
     * Récupère tous les sujets disponibles.
     * 
     * @return Liste de tous les sujets
     */
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }
    
    /**
     * Met à jour un sujet existant.
     * 
     * @param id Identifiant du sujet à mettre à jour
     * @param subjectDetails Nouvelles données du sujet
     * @return Le sujet mis à jour
     * @throws RuntimeException Si le sujet n'existe pas
     */
    public Subject updateSubject(Long id, Subject subjectDetails) {
        Subject subject = getSubjectById(id);
        subject.setName(subjectDetails.getName());
        return subjectRepository.save(subject);
    }
    
    /**
     * Supprime un sujet spécifique.
     * 
     * @param id Identifiant du sujet à supprimer
     */
    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }

    /**
     * Abonne un utilisateur à un sujet spécifique.
     * Si l'utilisateur est déjà abonné, aucune action n'est effectuée.
     * 
     * @param userId Identifiant de l'utilisateur
     * @param subjectId Identifiant du sujet
     * @throws RuntimeException Si l'utilisateur ou le sujet n'existe pas
     */
    public void subscribeUser(Long userId, Long subjectId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Subject subject = getSubjectById(subjectId);
        
        if (!user.getSubscriptions().contains(subject)) {
            user.getSubscriptions().add(subject);
            userRepository.save(user);
        }
    }

    /**
     * Désabonne un utilisateur d'un sujet spécifique.
     * 
     * @param userId Identifiant de l'utilisateur
     * @param subjectId Identifiant du sujet
     * @throws RuntimeException Si l'utilisateur ou le sujet n'existe pas
     */
    public void unsubscribeUser(Long userId, Long subjectId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Subject subject = getSubjectById(subjectId);
        
        user.getSubscriptions().remove(subject);
        userRepository.save(user);
    }
}