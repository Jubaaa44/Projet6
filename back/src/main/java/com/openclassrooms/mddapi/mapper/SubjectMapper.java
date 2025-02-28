package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.SubjectDTO;
import com.openclassrooms.mddapi.model.Subject;

/**
 * Mapper pour convertir entre les entités Subject et leurs DTOs.
 * Permet la transformation des objets du modèle en objets de transfert de données et vice-versa.
 */
public interface SubjectMapper {
    
    /**
     * Convertit une entité Subject en SubjectDTO.
     * Inclut les identifiants des posts associés et des abonnés ainsi que leurs compteurs.
     *
     * @param subject L'entité Subject à convertir
     * @return Le SubjectDTO correspondant, ou null si l'entité d'entrée est null
     */
    SubjectDTO toDto(Subject subject);

    /**
     * Convertit un SubjectDTO en entité Subject.
     * Note: Cette méthode ne remplit pas les associations (posts, subscribers),
     * elles doivent être gérées dans la couche service.
     *
     * @param dto Le SubjectDTO à convertir
     * @return L'entité Subject correspondante, ou null si le DTO d'entrée est null
     */
    Subject toEntity(SubjectDTO dto);
}