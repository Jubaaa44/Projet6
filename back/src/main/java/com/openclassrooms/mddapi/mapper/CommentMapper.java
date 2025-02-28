package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.model.Comment;

/**
 * Mapper pour convertir entre les entités Comment et leurs DTOs.
 * Permet la transformation des objets du modèle en objets de transfert de données et vice-versa.
 */
public interface CommentMapper {
    
    /**
     * Convertit une entité Comment en CommentDTO.
     *
     * @param comment L'entité Comment à convertir
     * @return Le CommentDTO correspondant, ou null si l'entité d'entrée est null
     */
    CommentDTO toDto(Comment comment);

    /**
     * Convertit un CommentDTO en entité Comment.
     * Note: Cette méthode ne remplit pas les associations (author, post),
     * elles doivent être gérées dans la couche service.
     *
     * @param dto Le CommentDTO à convertir
     * @return L'entité Comment correspondante, ou null si le DTO d'entrée est null
     */
    Comment toEntity(CommentDTO dto);
}