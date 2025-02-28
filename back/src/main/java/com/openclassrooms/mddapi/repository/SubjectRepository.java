package com.openclassrooms.mddapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.openclassrooms.mddapi.model.Subject;

/**
 * Repository pour gérer les opérations de persistance des sujets/thématiques.
 * Fournit les opérations CRUD standards ainsi que des méthodes personnalisées pour rechercher des sujets.
 */
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    
    /**
     * Trouve un sujet par son nom.
     * 
     * @param name Nom du sujet à rechercher
     * @return Le sujet correspondant au nom spécifié
     */
    Subject findByName(String name);
    
    /**
     * Vérifie si un sujet avec le nom spécifié existe déjà.
     * Utile pour la validation lors de la création d'un nouveau sujet.
     * 
     * @param name Nom du sujet à vérifier
     * @return true si un sujet avec ce nom existe, false sinon
     */
    boolean existsByName(String name);
}