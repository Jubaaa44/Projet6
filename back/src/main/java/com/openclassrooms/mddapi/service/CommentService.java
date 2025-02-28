package com.openclassrooms.mddapi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import java.util.List;

/**
 * Service gérant les opérations métier liées aux commentaires.
 * Cette classe implémente les fonctionnalités CRUD pour les commentaires ainsi que
 * les opérations spécifiques comme la récupération des commentaires par post.
 */
@Service
@Transactional
public interface CommentService {
    
    /**
     * Crée un nouveau commentaire pour un post spécifique.
     * 
     * @param postId Identifiant du post auquel ajouter le commentaire
     * @param commentDTO Données du commentaire à créer
     * @return Le DTO du commentaire créé
     * @throws ResourceNotFoundException Si le post ou l'auteur n'existe pas
     */
    public CommentDTO createComment(Long postId, CommentDTO commentDTO);
    
    /**
     * Récupère tous les commentaires associés à un post spécifique.
     * 
     * @param postId Identifiant du post
     * @return Liste des DTOs de commentaires associés au post
     */
    public List<CommentDTO> getCommentsByPost(Long postId);
    
    /**
     * Récupère un commentaire par son identifiant.
     * 
     * @param id Identifiant du commentaire
     * @return Le DTO du commentaire
     * @throws ResourceNotFoundException Si le commentaire n'existe pas
     */
    public CommentDTO getCommentById(Long id);
    
    /**
     * Met à jour un commentaire existant.
     * Seul le contenu du commentaire peut être modifié.
     * 
     * @param id Identifiant du commentaire à mettre à jour
     * @param commentDTO Nouvelles données du commentaire
     * @return Le DTO du commentaire mis à jour
     * @throws ResourceNotFoundException Si le commentaire n'existe pas
     */
    public CommentDTO updateComment(Long id, CommentDTO commentDTO);
    
    /**
     * Supprime un commentaire spécifique.
     * 
     * @param id Identifiant du commentaire à supprimer
     * @throws ResourceNotFoundException Si le commentaire n'existe pas
     */
    public void deleteComment(Long id);
}