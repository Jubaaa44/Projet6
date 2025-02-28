package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.model.User;

/**
 * Mapper pour convertir entre les entités User et leurs DTOs.
 * Permet la transformation des objets du modèle en objets de transfert de données et vice-versa.
 * Gère les considérations de sécurité liées au mot de passe.
 */
public interface UserMapper {
    
    /**
     * Convertit une entité User en UserDTO.
     * Pour des raisons de sécurité, le mot de passe n'est pas inclus dans le DTO résultant.
     * Inclut les identifiants et noms des abonnements, ainsi que le nombre de posts et commentaires.
     *
     * @param user L'entité User à convertir
     * @return Le UserDTO correspondant, ou null si l'entité d'entrée est null
     */
    UserDTO toDto(User user);

    /**
     * Convertit un UserDTO en entité User.
     * Inclut le transfert du mot de passe pour les opérations de création et d'authentification.
     * Note: Cette méthode ne remplit pas les associations (subscriptions, posts, comments),
     * elles doivent être gérées dans la couche service.
     *
     * @param dto Le UserDTO à convertir
     * @return L'entité User correspondante, ou null si le DTO d'entrée est null
     */
    User toEntity(UserDTO dto);
}