package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.model.User;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

/**
 * Mapper pour convertir entre les entités User et leurs DTOs.
 * Permet la transformation des objets du modèle en objets de transfert de données et vice-versa.
 * Gère les considérations de sécurité liées au mot de passe.
 */
@Component
public class UserMapper {
    
    /**
     * Convertit une entité User en UserDTO.
     * Pour des raisons de sécurité, le mot de passe n'est pas inclus dans le DTO résultant.
     * Inclut les identifiants et noms des abonnements, ainsi que le nombre de posts et commentaires.
     *
     * @param user L'entité User à convertir
     * @return Le UserDTO correspondant, ou null si l'entité d'entrée est null
     */
    public UserDTO toDto(User user) {
        if (user == null) {
            return null;
        }
        
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        // Ne pas mapper le password dans le DTO pour des raisons de sécurité
        
        if (user.getSubscriptions() != null) {
            dto.setSubscriptionIds(user.getSubscriptions().stream()
                .map(subject -> subject.getId())
                .collect(Collectors.toList()));
            dto.setSubscriptionNames(user.getSubscriptions().stream()
                .map(subject -> subject.getName())
                .collect(Collectors.toList()));
        }
        
        if (user.getPosts() != null) {
            dto.setPostCount(user.getPosts().size());
        }
        
        if (user.getComments() != null) {
            dto.setCommentCount(user.getComments().size());
        }
        
        return dto;
    }

    /**
     * Convertit un UserDTO en entité User.
     * Inclut le transfert du mot de passe pour les opérations de création et d'authentification.
     * Note: Cette méthode ne remplit pas les associations (subscriptions, posts, comments),
     * elles doivent être gérées dans la couche service.
     *
     * @param dto Le UserDTO à convertir
     * @return L'entité User correspondante, ou null si le DTO d'entrée est null
     */
    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // Transfert du mot de passe (sera encodé par le service)
        return user;
    }
}