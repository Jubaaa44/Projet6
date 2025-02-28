package com.openclassrooms.mddapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.mapper.CommentMapper;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service gérant les opérations métier liées aux commentaires.
 * Cette classe implémente les fonctionnalités CRUD pour les commentaires ainsi que
 * les opérations spécifiques comme la récupération des commentaires par post.
 */
@Service
@Transactional
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    
    /**
     * Constructeur avec injection des dépendances.
     * 
     * @param commentRepository Repository pour accéder aux commentaires
     * @param postRepository Repository pour accéder aux posts
     * @param userRepository Repository pour accéder aux utilisateurs
     * @param commentMapper Mapper pour convertir entre Comment et CommentDTO
     */
    @Autowired
    public CommentService(CommentRepository commentRepository, 
                         PostRepository postRepository,
                         UserRepository userRepository,
                         CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }
    
    /**
     * Crée un nouveau commentaire pour un post spécifique.
     * 
     * @param postId Identifiant du post auquel ajouter le commentaire
     * @param commentDTO Données du commentaire à créer
     * @return Le DTO du commentaire créé
     * @throws ResourceNotFoundException Si le post ou l'auteur n'existe pas
     */
    public CommentDTO createComment(Long postId, CommentDTO commentDTO) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
            
        User author = userRepository.findById(commentDTO.getAuthorId())
            .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
            
        Comment comment = commentMapper.toEntity(commentDTO);
        comment.setPost(post);
        comment.setAuthor(author);
        
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }
    
    /**
     * Récupère tous les commentaires associés à un post spécifique.
     * 
     * @param postId Identifiant du post
     * @return Liste des DTOs de commentaires associés au post
     */
    public List<CommentDTO> getCommentsByPost(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupère un commentaire par son identifiant.
     * 
     * @param id Identifiant du commentaire
     * @return Le DTO du commentaire
     * @throws ResourceNotFoundException Si le commentaire n'existe pas
     */
    public CommentDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        return commentMapper.toDto(comment);
    }
    
    /**
     * Met à jour un commentaire existant.
     * Seul le contenu du commentaire peut être modifié.
     * 
     * @param id Identifiant du commentaire à mettre à jour
     * @param commentDTO Nouvelles données du commentaire
     * @return Le DTO du commentaire mis à jour
     * @throws ResourceNotFoundException Si le commentaire n'existe pas
     */
    public CommentDTO updateComment(Long id, CommentDTO commentDTO) {
        Comment existingComment = commentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
            
        Comment comment = commentMapper.toEntity(commentDTO);
        existingComment.setContent(comment.getContent());
        
        Comment updatedComment = commentRepository.save(existingComment);
        return commentMapper.toDto(updatedComment);
    }
    
    /**
     * Supprime un commentaire spécifique.
     * 
     * @param id Identifiant du commentaire à supprimer
     * @throws ResourceNotFoundException Si le commentaire n'existe pas
     */
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Comment not found");
        }
        commentRepository.deleteById(id);
    }
}