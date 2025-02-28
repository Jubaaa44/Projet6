package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.PostDTO;
import com.openclassrooms.mddapi.model.Post;

/**
 * Mapper pour convertir entre les entités Post et leurs DTOs.
 * Permet la transformation des objets du modèle en objets de transfert de données et vice-versa.
 */
public interface PostMapper {
    
    /**
     * Convertit une entité Post en PostDTO.
     * Inclut la conversion des commentaires associés.
     *
     * @param post L'entité Post à convertir
     * @return Le PostDTO correspondant, ou null si l'entité d'entrée est null
     */
    PostDTO toDto(Post post);

    /**
     * Convertit un PostDTO en entité Post.
     * Note: Cette méthode ne remplit pas les associations (author, subject, comments),
     * elles doivent être gérées dans la couche service.
     *
     * @param dto Le PostDTO à convertir
     * @return L'entité Post correspondante, ou null si le DTO d'entrée est null
     */
    Post toEntity(PostDTO dto);
}