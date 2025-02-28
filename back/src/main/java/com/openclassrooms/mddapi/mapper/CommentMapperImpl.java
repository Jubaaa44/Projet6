package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.model.Comment;
import org.springframework.stereotype.Component;

/**
 * Implémentation du mapper pour convertir entre les entités Comment et leurs DTOs.
 */
@Component
public class CommentMapperImpl implements CommentMapper {
    
    /**
     * Convertit une entité Comment en CommentDTO.
     *
     * @param comment L'entité Comment à convertir
     * @return Le CommentDTO correspondant, ou null si l'entité d'entrée est null
     */
    @Override
    public CommentDTO toDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setDate(comment.getDate());
        
        if (comment.getAuthor() != null) {
            dto.setAuthorId(comment.getAuthor().getId());
            dto.setAuthorUsername(comment.getAuthor().getUsername());
        }
        
        if (comment.getPost() != null) {
            dto.setPostId(comment.getPost().getId());
        }
        
        return dto;
    }

    /**
     * Convertit un CommentDTO en entité Comment.
     * Note: Cette méthode ne remplit pas les associations (author, post),
     * elles doivent être gérées dans la couche service.
     *
     * @param dto Le CommentDTO à convertir
     * @return L'entité Comment correspondante, ou null si le DTO d'entrée est null
     */
    @Override
    public Comment toEntity(CommentDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setContent(dto.getContent());
        comment.setDate(dto.getDate());
        return comment;
    }
}