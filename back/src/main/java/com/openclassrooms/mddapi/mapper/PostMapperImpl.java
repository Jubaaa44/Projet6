package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.PostDTO;
import com.openclassrooms.mddapi.model.Post;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

/**
 * Mapper pour convertir entre les entités Post et leurs DTOs.
 * Permet la transformation des objets du modèle en objets de transfert de données et vice-versa.
 * Utilise CommentMapper pour gérer les commentaires associés.
 */
@Component
public class PostMapper {
    
    private final CommentMapper commentMapper;
    
    /**
     * Constructeur avec injection de dépendance du CommentMapper.
     *
     * @param commentMapper Le mapper pour convertir les commentaires associés
     */
    public PostMapper(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }
    
    /**
     * Convertit une entité Post en PostDTO.
     * Inclut la conversion des commentaires associés via le CommentMapper.
     *
     * @param post L'entité Post à convertir
     * @return Le PostDTO correspondant, ou null si l'entité d'entrée est null
     */
    public PostDTO toDto(Post post) {
        if (post == null) {
            return null;
        }
        
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setDate(post.getDate());
        
        if (post.getAuthor() != null) {
            dto.setAuthorId(post.getAuthor().getId());
            dto.setAuthorUsername(post.getAuthor().getUsername());
        }
        
        if (post.getSubject() != null) {
            dto.setSubjectId(post.getSubject().getId());
            dto.setSubjectName(post.getSubject().getName());
        }
        
        if (post.getComments() != null) {
            dto.setComments(post.getComments().stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }

    /**
     * Convertit un PostDTO en entité Post.
     * Note: Cette méthode ne remplit pas les associations (author, subject, comments),
     * elles doivent être gérées dans la couche service.
     *
     * @param dto Le PostDTO à convertir
     * @return L'entité Post correspondante, ou null si le DTO d'entrée est null
     */
    public Post toEntity(PostDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Post post = new Post();
        post.setId(dto.getId());
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setDate(dto.getDate());
        return post;
    }
}