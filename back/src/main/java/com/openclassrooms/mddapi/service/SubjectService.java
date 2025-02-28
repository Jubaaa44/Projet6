package com.openclassrooms.mddapi.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.model.Subject;
import java.util.List;

/**
 * Service gérant les opérations métier liées aux sujets/thématiques.
 * Cette classe implémente les fonctionnalités CRUD pour les sujets ainsi que
 * des méthodes spécifiques pour gérer les abonnements des utilisateurs aux sujets.
 */
@Service
public interface SubjectService {
    
    /**
     * Crée un nouveau sujet.
     * Vérifie que le nom du sujet est unique avant création.
     * 
     * @param subject L'entité Subject à créer
     * @return Le sujet créé, incluant l'ID généré
     * @throws RuntimeException Si un sujet avec le même nom existe déjà
     */
    public Subject createSubject(Subject subject);
    
    /**
     * Récupère un sujet par son identifiant.
     * 
     * @param id Identifiant du sujet
     * @return Le sujet correspondant
     * @throws RuntimeException Si le sujet n'existe pas
     */
    public Subject getSubjectById(Long id);
    
    /**
     * Récupère tous les sujets disponibles.
     * 
     * @return Liste de tous les sujets
     */
    public List<Subject> getAllSubjects();
    
    /**
     * Met à jour un sujet existant.
     * 
     * @param id Identifiant du sujet à mettre à jour
     * @param subjectDetails Nouvelles données du sujet
     * @return Le sujet mis à jour
     * @throws RuntimeException Si le sujet n'existe pas
     */
    public Subject updateSubject(Long id, Subject subjectDetails);
    
    /**
     * Supprime un sujet spécifique.
     * 
     * @param id Identifiant du sujet à supprimer
     */
    public void deleteSubject(Long id);

    /**
     * Abonne un utilisateur à un sujet spécifique.
     * Si l'utilisateur est déjà abonné, aucune action n'est effectuée.
     * 
     * @param userId Identifiant de l'utilisateur
     * @param subjectId Identifiant du sujet
     * @throws RuntimeException Si l'utilisateur ou le sujet n'existe pas
     */
    public void subscribeUser(Long userId, Long subjectId);

    /**
     * Désabonne un utilisateur d'un sujet spécifique.
     * 
     * @param userId Identifiant de l'utilisateur
     * @param subjectId Identifiant du sujet
     * @throws RuntimeException Si l'utilisateur ou le sujet n'existe pas
     */
    public void unsubscribeUser(Long userId, Long subjectId);
}