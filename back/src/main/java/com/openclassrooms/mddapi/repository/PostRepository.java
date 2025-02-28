package com.openclassrooms.mddapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.openclassrooms.mddapi.model.Post;
import java.util.List;

/**
 * Repository pour gérer les opérations de persistance des posts/articles.
 * Fournit les opérations CRUD standards ainsi que des méthodes personnalisées pour rechercher des posts.
 */
public interface PostRepository extends JpaRepository<Post, Long> {
    
    /**
     * Trouve tous les posts créés par un utilisateur spécifique.
     * 
     * @param authorId Identifiant unique de l'auteur (utilisateur)
     * @return Une liste des posts créés par l'utilisateur spécifié
     */
    List<Post> findByAuthorId(Long authorId);
    
    /**
     * Trouve tous les posts associés à un sujet spécifique.
     * 
     * @param subjectId Identifiant unique du sujet
     * @return Une liste des posts associés au sujet spécifié
     */
    List<Post> findBySubjectId(Long subjectId);
    
    /**
     * Récupère le fil d'actualité personnalisé pour un utilisateur spécifique.
     * Le fil contient les posts des sujets auxquels l'utilisateur est abonné, triés par date décroissante.
     * 
     * @param userId Identifiant unique de l'utilisateur
     * @return Une liste des posts pour le fil d'actualité de l'utilisateur, triés du plus récent au plus ancien
     */
    @Query("SELECT p FROM Post p WHERE p.subject IN (SELECT s FROM User u JOIN u.subscriptions s WHERE u.id = :userId) ORDER BY p.date DESC")
    List<Post> findFeedByUserId(@Param("userId") Long userId);
    
    /*
    @Query("SELECT p FROM Post p ORDER BY p.date DESC")
    List<Post> findFeedByUserId(@Param("userId") Long userId);
    */
}