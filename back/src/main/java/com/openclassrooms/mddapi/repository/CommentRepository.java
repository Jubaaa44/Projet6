package com.openclassrooms.mddapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.openclassrooms.mddapi.model.Comment;
import java.util.List;

/**
 * Repository pour gérer les opérations de persistance des commentaires.
 * Fournit les opérations CRUD standards ainsi que des méthodes personnalisées pour rechercher des commentaires.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    /**
     * Trouve tous les commentaires associés à un post spécifique.
     * 
     * @param postId Identifiant unique du post
     * @return Une liste des commentaires associés au post spécifié
     */
    List<Comment> findByPostId(Long postId);
    
    /**
     * Trouve tous les commentaires créés par un utilisateur spécifique.
     * 
     * @param authorId Identifiant unique de l'auteur (utilisateur)
     * @return Une liste des commentaires créés par l'utilisateur spécifié
     */
    List<Comment> findByAuthorId(Long authorId);
}